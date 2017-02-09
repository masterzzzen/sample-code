
var User = Backbone.Model.extend({
    defaults: {
        lastName: '',
        firstName: '',
        age: '',
        email: '',
        creationTime: new Date(), // full Date object - used for sorting
        createdOnDate: new Date().toLocaleDateString(), //  string containing date - used for display
        createdOnTimeOfDay: new Date().toLocaleTimeString(), // string containing time of day - used for display
        lastEditedTime: '', // full Date object - used for sorting
        lastEditedDate: '', //  string containing date - used for display
        lastEditedTimeOfDay: '', // string containing time of day - used for display
        isActive: true,
        passFilter: true,
    }
});

var Users = Backbone.Collection.extend({
    sortInfo: { // 0 means the table is not sorted on this element. 1 means ascending order. -1 means descending order
        lastName: 0,
        firstName: 0,
        age: 0,
        email: 0,
        creationTime: 0,
        lastEditedTime: 0,
        isActive: 0,
    }
});
var users = new Users();


// Generate 25 for our views to initialize
(function () {

    var firstNames = ['John', 'Arcy', 'Michael', 'Steve', 'Seth', 'Katie', 'Janice', 'Paul', 'Amy', 'Carlin', 'Kelly', 'Peter', 'Zach', 'Flan',
        'Dori', 'Nemo', 'Sam', 'Tori', 'Ann', 'Fiona', 'Gloria', 'Nancy', 'George', 'Ed', 'Nema'];
    var lastNames = ['Peterson', 'Paulson', 'Sanders', 'Ferron', 'Liu', 'Wang', 'Takeshi', 'Lawrence', 'Schumer', 'Johnson', 'Michaels', 'Carpenter', 'Macy', 'Roache', 'Cook',
        'Manning', 'Bryant', 'Carson', 'Jennings'];
    var domains = ['gmail.com', 'hotmail.com', 'yahoo.com', 'outlook.com'];
    var newUsers = new Set();
    var count = 0;
    while (count < 25) {

        var lastName = lastNames[Math.floor((Math.random() * (lastNames.length - 1)))];
        var firstName = firstNames[Math.floor((Math.random() * (firstNames.length - 1)))];
        var domain = domains[Math.floor((Math.random() * (domains.length - 1)))];
        var email = firstName.toLowerCase() + '_' + lastName.toLowerCase() + '@' + domain;
        var age = Math.floor(Math.random() * 40 + 18).toString();
        if (!newUsers.has(email)) {
            var model = new User({lastName: lastName, firstName: firstName, age: age, email: email});
            users.add(model);
            newUsers.add(email);
            count++;
        }
    }
})();


// Backbone View for one user
var UserView = Backbone.View.extend({
    model: new User(),
    tagName: 'tr',
    initialize: function () {
        this.template = _.template($('.users-list-template').html());
    },
    events: {
        'click .edit-user': 'edit',
        'click .update-user': 'update',
        'click .cancel-edit': 'cancel',
        'click .active': 'deactivate',
        'click .inactive': 'reactivate',
        'click .delete-user': 'delete',
    },
    edit: function () {

        this.$('.edit-user').hide();
        this.$('.update-user').show();
        this.$('.delete-user').hide();
        this.$('.cancel-edit').show();
        this.$('.non-editing-active').hide();

        if (this.model.get('isActive')) {
            this.$('.inactive').hide();
            this.$('.active').show();
        } else {
            this.$('.active').hide();
            this.$('.inactive').show();
        }

        var lastName = this.$('.last-name').html();
        var firstName = this.$('.first-name').html();
        var age = this.$('.age').html();
        var email = this.$('.email').html();

        this.$('.last-name').html('<input type="text" class="user-input last-name-input" value="' + lastName + '">');
        this.$('.first-name').html('<input type="text" class="user-input first-name-input" value="' + firstName + '">');
        this.$('.age').html('<input type="text" class="user-input age-input" value="' + age + '">');
        this.$('.email').html('<input type="text" class="user-input email-input" value="' + email + '">');
    },
    update: function () {

        var lastName = this.$('.last-name-input').val();
        var firstName = this.$('.first-name-input').val();
        var age = this.$('.age-input').val();
        var email = this.$('.email-input').val();


        if (lastName.trim() === '' || firstName.trim() === '' || email.trim() === '') {
            window.alert('Last Name, First Name, and Email fields cannot be blank');
            return;
        }

        var self = this;
        var terminate = false;
        _.forEach(users.toArray(), function (model) {
            if (model !== self.model && model.get('email') === email) {
                window.alert('You\'re entering a duplicate user');
                terminate = true;
                return;
            }
        });

        if (terminate) {
            return;
        }

        var time = new Date();
        var lastEditedDate = time.toLocaleDateString("en-US");
        var lastEditedTimeOfDay = time.toLocaleTimeString("en-US");
        var createdOnDate = this.$('.created-on').html().split('<br>')[0];
        var createdOnTimeOfDay = this.$('.created-on').html().split('<br>')[1];

        var isActive = false;
        if (this.$('.inactive').css('display') === 'none') {
            isActive = true;
        }

        this.model.set({
            lastName: lastName,
            firstName: firstName,
            age: age,
            email: email,
            createdOnDate: createdOnDate,
            createdOnTimeOfDay: createdOnTimeOfDay,
            lastEditedTime: time,
            lastEditedDate: lastEditedDate,
            lastEditedTimeOfDay: lastEditedTimeOfDay,
            isActive: isActive,
        });

        this.$('.update-user').hide();
        this.$('.edit-user').show();
        this.$('.cancel-edit').hide();
        this.$('.delete-user').show();
        this.render();
    },
    cancel: function () {
        this.render();
    },
    deactivate: function () {
        this.$('.active').hide();
        this.$('.inactive').show();
    },
    reactivate: function () {
        this.$('.active').show();
        this.$('.inactive').hide();
    },
    delete: function () {
        this.model.destroy();
        updateUserRange();
    },
    render: function () {
        this.$el.html(this.template(this.model.toJSON()));
        return this;
    }
});

// Backbone View for a page of users
var UsersView = Backbone.View.extend({
    model: users,
    el: $('.table'),
    events: {
        'click .header': 'sort',
    },
    initialize: function () {
        this.pageNum = 0;
        this.pageSize = 10; // showing at most 10 user views per page
        this.filterMode = false; // helps to determine whether to display a 10 item window of data or all filtered results
        this.model.on('add', this.render, this);
        this.model.on('remove', this.render, this);
        this.render();
    },
    setSortInfo: function (sortBy) {
        var info = this.model.sortInfo;
        for (var property in info) {
            if (info.hasOwnProperty(property)) {
                if (property === sortBy) {
                    if (info[property] === 0 || info[property] === -1) {
                        info[property] = 1;
                    } else { // if (info[sortBy] === 1)
                        info[property] = -1;
                    }
                } else {
                    info[property] = 0;
                }
            }
        }
    },
    sort: function (evt) {
        var sortBy = evt.target.id;
        var propertyName = '';
        switch (sortBy) {
            case 'last-name-header':
                propertyName = 'lastName';
                break;
            case 'first-name-header':
                propertyName = 'firstName';
                break;
            case 'age-header':
                propertyName = 'age';
                break;
            case 'email-header':
                propertyName = 'email';
                break;
            case 'created-on-header':
                propertyName = 'creationTime';
                break;
            case 'last-edited-header':
                propertyName = 'lastEditedTime';
                break;
            case 'active-header':
                propertyName = 'isActive';
                break;
        }
        this.setSortInfo(propertyName);
        var self = this;
        var sorted = _.sortBy(self.model.toArray(), function (model) {
            return model.get(propertyName);
        });

        if (this.model.sortInfo[propertyName] === -1) {
            sorted = sorted.reverse();
        }

        this.model = new Users(sorted);

        this.render();
    },
    render: function () {
        var self = this;

        var first = this.pageNum * this.pageSize;
        var ifAllTen = first + 9;
        var last = ifAllTen > this.model.length ? this.model.length : ifAllTen;
        this.$('.users-list').html('');

        var whichCheck = ''; // the property by which to check if a user should be displayed
        if (!this.filterMode) {
            var arr = this.model.toArray();
            for (var i = 0; i < arr.length; i++) {
                if (first <= i && i <= last) { // check if model should be in the 10 item display window
                    arr[i].set('inWindow', true);
                } else {
                    arr[i].set('inWindow', false);
                }
            }
            whichCheck = 'inWindow';
        } else {
            whichCheck = 'passFilter';
        }

        _.each(self.model.toArray(), function (user) {
            if (user.get(whichCheck)) {
                self.$('.users-list').append(new UserView({model: user}).render().$el);
            }
        });
        return this;
    }
});
var usersView = new UsersView();


var updateUserRange = function () {

    var range = '';
    var usersLength = users.length;
    if (usersLength == 0) {
        range = '0';
    } else { // usersLength > 10
        var first = usersView.pageNum * usersView.pageSize + 1;
        var ifAllTen = first + 9;
        var last = ifAllTen > usersLength ? usersLength : ifAllTen;
        range = first + ' - ' + last;
    }

    if (usersView.pageNum > 0) {
        $('.prev-button').show();
    } else {
        $('.prev-button').hide();
    }

    var userCountDisplay = $('.user-count-display');
    userCountDisplay.html('<span>Showing ' + range + ' of ' + usersLength + ' users </span>');

    if ((usersView.pageNum + 1) * usersView.pageSize < usersLength) {
        $('.next-button').show();
    } else {
        $('.next-button').hide();
    }

}


$(document).ready(function () {

    updateUserRange();

    $('.cancel-new-add').on('click', function () {
        $('.new-user-input-container').hide();
    });

    $('.new-active').on('click', function () {
        $('.new-active').hide();
        $('.new-inactive').show();
    });

    $('.new-inactive').on('click', function () {
        $('.new-inactive').hide();
        $('.new-active').show();
    });

    $('.big-add-user-button').on('click', function () {

        var lastName = $('.new-last-name-input').val();
        var firstName = $('.new-first-name-input').val();
        var age = $('.new-age-input').val();
        var email = $('.new-email-input').val();

        var isActive = false;
        if ($('.new-inactive').css('display') === 'none') {
            isActive = true;
        }

        if (lastName.trim() === '' || firstName.trim() === '' || email.trim() === '') {
            window.alert('Last Name, First Name, and Email fields cannot be blank');
            return;
        }

        var terminate = false;
        _.forEach(users.toArray(), function (model) {
            if (model.get('email') === email) {
                window.alert('You\'re entering a duplicate user');
                terminate = true;
                return;
            }
        });

        if (terminate) {
            return;
        }

        var newDate = new Date();
        var user = new User({
            lastName: lastName,
            firstName: firstName,
            age: age,
            email: email,
            creationTime: newDate,
            createdOnDate: newDate.toLocaleDateString("en-US"),
            createdOnTimeOfDay: newDate.toLocaleTimeString("en-US"),
            lastEditedDate: '',
            lastEditedTimeOfDay: '',
            isActive: isActive
        });
        users.add(user);
        updateUserRange();
        usersView.render();
    });

    $('.search-box').on('focusin', function (evt) {
        evt.target.select();
    });

    $('.search-box').on('focusout', function (evt) {
        if ($(evt.target).val().trim() === '') {
            usersView.filterMode = false;
            updateUserRange();
            evt.target.value = 'Filter';
        }
    });

    $('.search-box').on('keyup', function (evt) {
        var searchTerm = $(evt.target).val().trim();
        usersView.filterMode = true;
        _.forEach(users.toArray(), function (model) {
            model.set('passFilter', false);
        });

        var filteredCount = 0;
        _.forEach(users.toArray(), function (model) {
            var found = false;
            for (var property in model.attributes) {
                var value = model.get(property);
                if (typeof value === 'string' && value.includes(searchTerm)) {
                    found = true;
                    model.set('passFilter', true);
                    break;
                }
            }
            if (found) {
                filteredCount++;
            }
        });
        var userCountDisplay = $('.user-count-display');
        $('.prev-button').hide();
        $('.next-button').hide();
        userCountDisplay.html('<span>Showing ' + filteredCount + ' filtered results </span>');
        usersView.render();
    });

    $('.user-input').on('click', function (evt) {
        evt.target.select();
    });

    $('.next-button').on('click', function () {
        if (usersView.pageNum < (users.length - 1) / usersView.pageSize) {
            usersView.pageNum++;
        }
        updateUserRange();
        usersView.render();
    });

    $('.prev-button').on('click', function () {
        if (usersView.pageNum > 0) {
            usersView.pageNum--;
        }
        updateUserRange();
        usersView.render();
    });

});