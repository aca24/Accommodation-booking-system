var role = '';

$(document).ready(function(){
    
    var loggedUser;
    let queryParam = new URLSearchParams (window.location.search);
    let apartmentID = queryParam.get('id');

    $.get({
        url: '/PocetniREST/rest/users/currentUser',
        success: function(user){
            loggedUser = user;
        },
        complete: function(){
            // ucitavanje podataka o apartmanu
            $.get({
                url: '/PocetniREST/rest/apartments/' + apartmentID,
                cache: false,
                success: function(apartment){
                    loadApartment(apartment, loggedUser);   
                }
            });
        }
    });
    
    $.get({
        url: '/PocetniREST/rest/users/role',
        success: function(data){
            role = data;
        }
    });
});

function showImage(img) {
    let expandImg = $('#expandedImg');
    expandImg.attr('src', img.attr('src'));
};

function loadApartment(apartment, loggedUser){
    $('#apartment').html(createApartment(apartment, loggedUser));

    // rating za apartman
    if(parseInt(apartment.rating) > 4.5){
        ratingStar(5, "");
    }
    else if(parseInt(apartment.rating) > 3.5){
        ratingStar(4, "");
    }
    else if(parseInt(apartment.rating) > 2.5){
        ratingStar(3, "");
    }
    else if(parseInt(apartment.rating) > 1.5){
        ratingStar(2, "");
    }
    else if(parseInt(apartment.rating) > 0.5){
        ratingStar(1, "");
    }
    else{
                // nema recenzija
    }

    loadComments(apartment);

    // rating za leave comment
    for(let i = 1; i <= 5; i++){
        $('#c' + i).click(function(){
            addStar(i);
        });
    }

    // leave a comment
    $('#leaveComment').click(function(){
        let comment = $('#comment').val();
        let rating;
        if($('#c5').hasClass('checked')){
            rating = 5;
        }
        else if($('#c4').hasClass('checked')){
            rating = 4;
        }
        else if($('#c3').hasClass('checked')){
            rating = 3;
        }
        else if($('#c2').hasClass('checked')){
            rating = 2;
        }
        else if($('#c1').hasClass('checked')){
            rating = 1;
        }

        $.ajax({
            url: '/PocetniREST/rest/comments/comment',
            type: 'POST',
            data: JSON.stringify({guest: loggedUser.username, apartmentId: apartment.id, text: comment, rating: rating}),
            contentType: 'application/json',
            success: function(text){
                loadComments(apartment);
                $('#comment').empty();
                $('#msgComment').text(text);
                $('#msgComment').removeClass('text-danger');
                $('#msgComment').addClass('text-info');
                $('#msgComment').show().delay(3000).fadeOut(2000);
            },
            error: function(jqxhr){
                $('#msgComment').text(jqxhr.responseText);
                $('#msgComment').removeClass('text-info');
                $('#msgComment').addClass('text-danger');
                $('#msgComment').show();
            }
        });
         
    });

    // ucitavanje slika
    $.get({
        url: '/PocetniREST/rest/images/apartment/' + apartment.id,
        success: (images)=>{
            let length = images.length;
            let width = (100/length).toFixed(2);
            if(length > 0){
                let i = 1;
                images.forEach(img => {
                    let url = ".." + img.imageURL;
                    if(i == 1){
                        $('#expandedImg').attr('src', url+'?rand='+new Date());
                    }
                    if(length != 1){
                        $('#maleSlike').append('<div class="column" style="width: ' + width + '%;height: ' + width + '%;"><img src="' + url +'?rand='+ new Date() + '" class="img-thumbnail" alt="image" id="img' + i + '"></div>');
                    }
                    i++;
                });                
            } 

            // male slike funkcionalnost
            for(let i = 1; i <= 5; i++){
                $('#img'+i).click(function(){
                showImage($('#img'+i));
                });
            }
        }
    });

    let enabledDays = new Array();
    apartment.availableDates.forEach(timestamp => {
        enabledDays.push(formatDate(new Date(timestamp)));
    });

    // datepicker
    $('#datepicker').datepicker({
        format: "dd-mm-yyyy",
        weekStart: 1,
        startDate: "today",
        maxViewMode: 2,
        daysOfWeekHighlighted: "0",
        todayHighlight: true,
        beforeShowDay: function(date){
            if (enabledDays.indexOf(formatDate(date)) < 0)
              return {
                enabled: false,
              };
            else
              return {
                enabled: true,
              };
          }
    });

    // amenities
    let amenities = apartment.amenities;
    let i = 1;
    amenities.forEach(amenity => {
        if(i == 4){
            i = 1;
        }
        $('#col'+i).append('<p class="text-muted"><i class="fa fa-check-square-o"></i> ' + amenity.name + '</p>');
        i++;
    });

    // rezervacija
    $('#reserve').click(function(){
        $('#numberOfStays').removeClass('border-danger');
        if(loggedUser == undefined){
            $('#msg').text('Please, sign in to create a reservation.');
            $('#msg').removeClass('text-info');
            $('#msg').addClass('text-danger');
            $('#msg').show();
        }
        else{
            let date = $('#datepicker').datepicker('getFormattedDate');
            let numberOfStays = $('#numberOfStays').val();
            let message = $('#message').val(); 

            $.ajax({
                url: '/PocetniREST/rest/reservations/reserve',
                type: 'POST',
                data: JSON.stringify({apartmentId: apartment.id, guest: loggedUser.username, date: date, numberOfStays: numberOfStays, price: apartment.price, message: message, availableDates: apartment.availableDates}),
                contentType: 'application/json',
                success: function(text){
                    $('#msg').text(text);
                    $('#msg').removeClass('text-danger');
                    $('#msg').addClass('text-info');
                    $('#msg').show().delay(3000).fadeOut(2000);
                    $('#message').val('');
                    $('#numberOfStays').val('');
                },
                error: function(jqxhr){
                    $('#msg').text(jqxhr.responseText);
                    $('#msg').removeClass('text-info');
                    $('#msg').addClass('text-danger');
                    $('#msg').show();
                    $('#numberOfStays').val('');
                    if((jqxhr.responseText == "Please, enter number of stays.") || (jqxhr.responseText == "Please, change number of stays to match available dates.")){
                        $('#numberOfStays').addClass('border-danger');
                    }
                }
            });
        }

        
    });
}

function formatDate(d) {
    var day = String(d.getDate());
    if (day.length == 1)
      day = '0' + day;
    var month = String((d.getMonth()+1));
    if (month.length == 1)
      month = '0' + month;
    return day + "-" + month + "-" + d.getFullYear();
};

function addStar(num){
    for(let i = 2; i <= 5; i++){
      $('#c'+i).removeClass('checked');
    }

    while(num > 0){
      $('#c'+num).addClass('checked');
      num--;
    }
};

function ratingStar(num, id){
    for(let i = 1; i <= num; i++){
        $('#' + id + i).addClass('checked');
      }
};

function createApartment(apartment, loggedUser){
    let address = apartment.address;
    let city = address.city;
    let street = address.street;
    let number = address.number;
    let lon = apartment.longitude;
    let lat = apartment.latitude;

    let html = "";

                // Naziv, lokacija, ocena
    html +=     '<div class="row">'+
                    '<div class="col-6 pl-0" id="leftPanel">'+
                        '<p class="bg-info text-white h5 p-1 rounded">' + apartment.name + '</p>'+
                        '<br>'+
                        '<p class="bg-light p-1 rounded"><i class="fa fa-map-marker"></i> ' + city + ', ' + street + ' ' + number + ' <a href="http://localhost:8080/PocetniREST/html/map.html?lon=' + lon + '&lat=' + lat + '">(Show on map)</a></p>'+
                    '</div>'+
                    '<div class="col-6 pr-0 text-right" id="rightPanel">'+
                        '<p class="mb-xl-1 p-1 mb-2">'+
                            '<span class="fa fa-star" id="1"></span>'+
                            '<span class="fa fa-star" id="2"></span>'+
                            '<span class="fa fa-star" id="3"></span>'+
                            '<span class="fa fa-star" id="4"></span>'+
                            '<span class="fa fa-star" id="5"></span>'+
                        '</p>'+
                        '<br>'+
                        '<p class="bg-light p-1 rounded">' + apartment.reviews;
                        if(apartment.reviews == 1){
                            html += ' review</p>';
                        }
                        else{
                            html += ' reviews</p>';
                        }
    html +=         '</div>'+
                '</div>';

                // Male slike
    html +=     '<div class="row" id="maleSlike">'+
                
                '</div>';
                
                // Velika slika
    html +=     '<div class="row">'+
                    '<img src="../img/Capture.jpg" id="expandedImg" style="width:100%">'+
                '</div>';

                // Informacije
    html +=     '<div class="row mt-3 border-bottom">'+
                    '<div class="col">'+
                        '<p class="h6 text-info">Information</p>'+
                    '</div>'+
                    '<div class="col text-right">'+
                        '<button class="fa fa-angle-down btn" type="button" data-toggle="collapse" data-target="#info" aria-expanded="false" aria-controls="info"></button>'+
                    '</div>'+
                    '<div class="col-12 collapse" id="info">'+
                        '<div class="card card-body border-0">'+
                            '<div class="row">'+
                                '<div class="col">'+
                                    '<p class="text-muted">Apartment type: ' + apartment.type + '</p>'+
                                    '<p class="text-muted">Number of rooms: ' + apartment.numberOfRooms + '</p>'+
                                    '<p class="text-muted">Number of guests: ' + apartment.numberOfGuests + '</p>'+
                                '</div>'+
                                '<div class="col">'+
                                    '<p class="text-muted">Price: $' + apartment.price + '</p>'+
                                    '<p class="text-muted">Check in: ' + apartment.checkInTime + 'h</p>'+
                                    '<p class="text-muted">Check out: ' + apartment.checkOutTime + 'h</p>'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>';

                // Rezervacija
    html +=     '<div class="row mt-3 border-bottom">'+
                    '<div class="col">'+
                        '<p class="h6 text-info">Available dates</p>'+
                    '</div>'+
                    '<div class="col text-right">'+
                        '<button class="fa fa-angle-down btn" type="button" data-toggle="collapse" data-target="#reservation" aria-expanded="false" aria-controls="reservation"></button>'+
                    '</div>'+
                    '<div class="col-12 collapse" id="reservation">'+
                        '<div class="card card-body border-0">'+
                            '<div class="row">'+
                                '<div class="col-lg-6 col-md-7 col-sm-7" id="datepicker"></div>';
                                if(role != 'HOST' && role != 'ADMIN'){
                                html += '<div class="col-lg-6 col-md-5 col-sm-5">'+
                                    '<div class="row mb-1">'+
                                        '<p class="text-muted">Number of stays:</p>'+
                                        '<input type="number" class="form-control" id="numberOfStays" min="1">'+
                                    '</div>'+
                                    '<div class="row mb-3">'+
                                        '<p class="text-muted">Message:</p>'+
                                        '<textarea class="form-control" id="message" rows="2"></textarea>'+
                                    '</div>'+
                                    '<div class="row justify-content-between">'+
                                        '<div class="col-auto mr-auto p-0">'+
                                            '<p id="msg"></p>'+
                                        '</div>'+
                                        '<div class="col-auto p-0">'+
                                            '<button type="button" class="btn btn-outline-secondary" id="reserve">Reserve</button>'+
                                        '</div>'+
                                    '</div>'+
                                '</div>';
                                }
                     html +='</div>'+
                        '</div>'+ 
                    '</div>'+
                '</div>';

                // Amenities
    html +=     '<div class="row mt-3 border-bottom">'+
                    '<div class="col">'+
                        '<p class="h6 text-info">Amenities</p>'+
                    '</div>'+
                    '<div class="col text-right">'+
                        '<button class="fa fa-angle-down btn" type="button" data-toggle="collapse" data-target="#amenities" aria-expanded="false" aria-controls="amenities"></button>'+
                    '</div>'+
                    '<div class="col-12 collapse" id="amenities">'+
                        '<div class="card card-body border-0">'+
                            '<div class="row">'+
                                '<div class="col" id="col1">'+
                                '</div>'+
                                '<div class="col" id="col2">'+
                                '</div>'+
                                '<div class="col" id="col3">'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>';	

                // Comments
    html +=     '<div class="row mt-3 border-bottom">'+
                    '<div class="col">'+
                        '<p class="h6 text-info">Comments</p>'+
                    '</div>'+
                    '<div class="col text-right">'+
                        '<button class="fa fa-angle-down btn" type="button" data-toggle="collapse" data-target="#comments" aria-expanded="false" aria-controls="comments"></button>'+
                    '</div>'+
                    '<div class="col-12 collapse" id="comments">'+
                        '<div class="card card-body border-0">';
                            // leave a comment
                            if(role != 'HOST' && role != 'ADMIN'){
                                html +='<div class="row border rounded mb-3">'+
                                        '<div class="col">'+
                                            '<div class="row pl-2">'+
                                                '<p class="lead h6 p-1">Leave a comment:</p>'+
                                            '</div>';
                                    if(loggedUser == undefined){
                                        html += '<div class="row mb-2 pl-2 pr-2">'+
                                                    '<textarea class="form-control" id="comment" rows="2" disabled="true" placeholder="Please create a reservation to be able to leave a comment."></textarea>'+
                                                '</div>'+
                                                '<div class="row mb-2 justify-content-between pl-2 pr-2">'+
                                                    '<div class="col-auto mr-auto p-0">'+
                                                            '<p id="msgComment"></p>'+
                                                    '</div>'+
                                                    '<div class="col-auto p-0">'+
                                                        '<button type="button" class="btn btn-outline-secondary" id="leaveComment" disabled="true">Comment</button>'+
                                                    '</div>'+
                                                '</div>';
                                    }
                                    else{
                                        html += '<div class="row pl-2">'+
                                                    '<p class="mb-xl-1 p-1">'+
                                                        '<span class="fa fa-star" id="c1"></span>'+
                                                        '<span class="fa fa-star" id="c2"></span>'+
                                                        '<span class="fa fa-star" id="c3"></span>'+
                                                        '<span class="fa fa-star" id="c4"></span>'+
                                                        '<span class="fa fa-star" id="c5"></span>'+
                                                    '</p>'+
                                                '</div>'+
                                                '<div class="row mb-2 pl-2 pr-2">'+
                                                    '<textarea class="form-control" id="comment" rows="2" placeholder="Enter your comment here."></textarea>'+
                                                '</div>'+
                                                '<div class="row mb-2 justify-content-between pl-2 pr-2">'+
                                                    '<div class="col-auto mr-auto p-0">'+
                                                            '<p id="msgComment"></p>'+
                                                    '</div>'+
                                                    '<div class="col-auto p-0">'+
                                                        '<button type="button" class="btn btn-outline-secondary" id="leaveComment">Comment</button>'+
                                                    '</div>'+
                                                '</div>';
                                    }
                                    
                        html += '</div>'+
                            '</div>';
                            }
                        html +='<div id="listOfComments">'+
                                    // comments
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>';

    return html;
};

function loadComments(apartment){

    $.get({
        url: '/PocetniREST/rest/apartments/' + apartment.id,
        success: function(newApartment){
            let html = '';
            let comments = newApartment.comments;
            if(comments.length == 0){
                html += '<p class="text-muted">Be the first to comment.</p>';
            }
            comments.forEach(c => {
                html += '<div class="row border rounded mb-2">'+
                            '<div class="col-6">'+
                                '<p class="lead h6 mb-1 p-1">' + c.guest + '</p>'+
                            '</div>'+
                        '<div class="col-6 text-right">'+
                            '<p class="mb-xl-1 p-1 mb-2">'+
                                '<span class="fa fa-star" id="' + c.id + '1"></span>'+
                                '<span class="fa fa-star" id="' + c.id + '2"></span>'+
                                '<span class="fa fa-star" id="' + c.id + '3"></span>'+
                                '<span class="fa fa-star" id="' + c.id + '4"></span>'+
                                '<span class="fa fa-star" id="' + c.id + '5"></span>'+
                            '</p>'+
                        '</div>'+
                    '<div class="col-12">'+
                        '<p class="text-muted p-2">' + c.text + '</p>'+
                    '</div>'+
                '</div>';
                
            });

            $('#listOfComments').html(html);

            // rating za komentare
            comments.forEach(c => {
                ratingStar(c.rating, c.id);
            });
        }
    });
    
};
