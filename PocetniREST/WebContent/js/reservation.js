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

function loadApartment(apartment, loggedUser){
    $('#name').html(addName(apartment));
    $('#apartment').html(createApartment());

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
                    $('#msg').text(jqxhr);
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

function addName(apartment){
    let html = "";
    html += apartment.name;
    return html;
}

function createApartment(){
    let html = "";
    html +=             '<div class="card card-body border-0">'+
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
                        '</div>';
    return html;
}
