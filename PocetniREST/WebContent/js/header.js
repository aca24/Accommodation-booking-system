$(document).ready(function(){
var role = 'UNKNOWN';
    $.get({
        url: '/PocetniREST/rest/users/currentUser',
        success: function(user){
            
            $.get({
                url: '/PocetniREST/rest/users/role',
                success: function(data){
                    role = data;

                    $('#header').html(createHTML(role));
        
                    $('#logOut').click(function(){
                        $.get({
                            url: '/PocetniREST/rest/users/logout',
                            success: function(){
                                window.location.href = "http://localhost:8080/PocetniREST/html/login.html";
                            }
                        });
                    });
        
                    $('#myProfile').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/myProfile.html";
                    });

                    $('#amenitiesAdmin').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/amenitiesAdmin.html";
                    });

                    $('#apartments').click(function(){
                        if(role == 'ADMIN'){
                            window.location.href = "http://localhost:8080/PocetniREST/html/adminApartments.html";
                        }
                        else{
                            window.location.href = "http://localhost:8080/PocetniREST/html/apartments.html";
                        }
                    });

                    $('#newApartment').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/newApartment.html";
                    });

                    $('#usersAdmin').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/usersAdmin.html";
                    });
                    
                    $('#usersHost').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/usersHost.html";
                    });

                    $('#inactiveApartments').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/hostInactiveApartments.html";
                    });

                    $('#activeApartments').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/hostActiveApartments.html";
                    });

                    $('#reservations').click(function(){
                        if(role == 'GUEST'){
                            window.location.href = "http://localhost:8080/PocetniREST/html/reservationsGuest.html";
                        } else if (role == 'HOST'){
                            window.location.href = "http://localhost:8080/PocetniREST/html/reservationsHost.html";
                        } else if (role == 'ADMIN'){
                            window.location.href = "http://localhost:8080/PocetniREST/html/reservationsAdmin.html";
                        }
                    });

                    $('#registerHost').click(function(){
                        window.location.href = "http://localhost:8080/PocetniREST/html/registration.html?host";
                    });

                    $('#commentsList').click(function(){
                        if(role == 'HOST') {
                            window.location.href = "http://localhost:8080/PocetniREST/html/commentsHost.html";
                        } else if(role == 'ADMIN') {
                            window.location.href = "http://localhost:8080/PocetniREST/html/commentsAdmin.html";
                        }
                    });

                    $('#apartmentSearchBtn').click(function(){
                        var aparmentName = $('#apartmentSearch').val();
                        if(role == 'ADMIN') {
                            $.get({
                                url: '/PocetniREST/rest/apartments/findByName/' + aparmentName,
                                success: function(apartmentId){
                                    window.location.href = "http://localhost:8080/PocetniREST/html/adminApartments.html?id=" + apartmentId;
                                },
                                error: function() {
                                    alert("Ne postoji apartman sa imenom " + aparmentName);
                                }
                            });
                        } else if (role == 'HOST') {
                            $.get({
                                url: '/PocetniREST/rest/apartments/findByNameHost/' + aparmentName,
                                success: function(apartmentId){
                                    window.location.href = "http://localhost:8080/PocetniREST/html/hostActiveApartments.html?id=" + apartmentId;
                                },
                                error: function() {
                                    alert("Ne postoji apartman sa imenom " + aparmentName);
                                }
                            });
                        } else if (role == 'GUEST'){
                            $.get({
                                url: '/PocetniREST/rest/apartments/findByNameActive/' + aparmentName,
                                success: function(apartmentId){
                                    window.location.href = "http://localhost:8080/PocetniREST/html/apartments.html?id=" + apartmentId;
                                },
                                error: function() {
                                    alert("Ne postoji apartman sa imenom " + aparmentName);
                                }
                            });
                        }
                    });
                }
            });
        },
        error: function(){
            $('#header').html(createHTML(role));

            $('#signIn').click(function(){
                window.location.href = "http://localhost:8080/PocetniREST/html/login.html";
            });
        
            $('#signUp').click(function(){
                window.location.href = "http://localhost:8080/PocetniREST/html/registration.html";
            });

            $('#apartments').click(function(){
                window.location.href = "http://localhost:8080/PocetniREST/html/apartments.html";
            });

            $('#apartmentSearchBtn').click(function(){
                var aparmentName = $('#apartmentSearch').val();
                $.get({
                    url: '/PocetniREST/rest/apartments/findByNameActive/' + aparmentName,
                    success: function(apartmentId){
                        window.location.href = "http://localhost:8080/PocetniREST/html/apartments.html?id=" + apartmentId;
                    },
                    error: function() {
                        alert("Ne postoji apartman sa imenom " + aparmentName);
                    }
                });
            });
        }
    }); 
});

function createHTML(role){
    let html = "";
    
    html += '<ul class="navbar-nav">' + '<li class="nav-item">'; 
    if(role == 'HOST'){ //  Samo HOST ima dropdown pomocu kojeg pristupa neaktivnim apartmanima, kreiranje novog itd.
        html += '<div class="btn-group">'+
                    '<a class="navbar-brand" href="#" id="activeApartments">Apartments</a>'+ 
                    '<a class="btn btn-transparent dropdown-toggle dropdown-toggle-split" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></a>'+
                    '<div class="dropdown-menu">'+
                        '<a class="dropdown-item" href="#" id="inactiveApartments">Inactive apartments</a>'+
                        '<div class="dropdown-divider"></div>'+
                        '<a class="dropdown-item" href="#" id="newApartment">Create new apartment</a>'+
                    '</div>'+
                '</div>'+
        '</li>'+
    '</ul>';
    }
    else {
        html += '<a class="navbar-brand" href="#" id="apartments">Apartments</a>'+
        '</li>'+
    '</ul>';
    }
                    
    if(role != 'UNKNOWN'){
        html += '<ul class="navbar-nav">'+
                '<li class="nav-item">'+ // GUEST, HOST, ADMIN
                    '<a class="nav-link" href="#" id="reservations">Reservations</a>'+ 
                '</li>';
        if(role == 'ADMIN' || role == 'HOST'){
            html += '<li class="nav-item">'+ // HOST, ADMIN
                        '<a class="nav-link" href="#" id="commentsList">Comments</a>'+ 
                    '</li>';
        }
        if(role == 'ADMIN'){
            html += '<li class="nav-item">'+ // ADMIN
                        '<a class="nav-link" href="#" id="amenitiesAdmin">Amenities</a>'+ 
                    '</li>'+
                    '<li class="nav-item">'+ // ADMIN
                        '<a class="nav-link" href="#" id="usersAdmin">Users</a>'+ 
                    '</li>'+
                    '<li class="nav-item">'+ // ADMIN
                        '<a class="nav-link" href="#" id="registerHost">Register host</a>'+ 
                    '</li>';
        }
        if(role == 'HOST'){
            html += '<li class="nav-item">'+ // HOST
                        '<a class="nav-link" href="#" id="usersHost">Users</a>'+ 
                    '</li>';
        }
        html += '</ul>';
    }
                    
    html += '<ul class="navbar-nav ml-auto">';
    let location = window.location.pathname;
    if(location == '/PocetniREST/html/apartments.html' || location == '/PocetniREST/html/hostActiveApartments.html'
    || location == '/PocetniREST/html/adminApartments.html'){
        html += '<li class="form-inline">' +
                    '<input class="form-control mr-sm-2 btn-light" type="search" id="apartmentSearch" placeholder="Search" aria-label="Search">' +
                    '<button class="btn btn-outline-light my-2 my-sm-0" type="button" id="apartmentSearchBtn">Search</button>' +
                '</li>';
    }
    if(role != 'UNKNOWN'){
        html += '<li class="nav-item">'+ // GUEST, HOST, ADMIN
                    '<a class="nav-link" href="#" id="myProfile">My profile</a>'+
                '</li>' +
                '<li class="nav-item">'+ // GUEST, HOST, ADMIN
                    '<a class="nav-link" href="#" id="logOut">Log out</a>'+
                '</li>';
    }
    if(role == 'UNKNOWN'){
        html += '<li class="nav-item">'+ // UNKNOWN
                    '<a class="nav-link" href="#" id="signUp">Sign up</a>'+
                '</li>'+
                '<li class="nav-item">'+ // UNKNOWN
                    '<a class="nav-link" href="#" id="signIn">Sign in</a>'+
                '</li>';
    }
                
    html += '</ul>';
    return html;
};