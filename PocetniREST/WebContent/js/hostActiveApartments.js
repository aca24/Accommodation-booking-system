$(document).ready(function () {

    var searchParams = new URLSearchParams(window.location.search)
    if (searchParams.has('id')) {
        let apartmentId = searchParams.get('id')
        $.get({
            url: '/PocetniREST/rest/apartments/searchById/' + apartmentId,
            success: function (apartmentsFiltered) {
                loadApartments(apartmentsFiltered);
            }
        });
    } else {
        $.get({
            url: '/PocetniREST/rest/apartments/hostApartmentsActive',
            success: function (apartments) {
                loadApartments(apartments);
            }
        });
    }

    // stars (rating)
    for (let i = 1; i <= 5; i++) {
        $('#star' + i).click(function () {
            addStar(i);
        });
    }

    // datepicker
    $('#datepicker').datepicker({
        format: "dd-mm-yyyy",
        weekStart: 1,
        startDate: "today",
        maxViewMode: 2,
        forceParse: false,
        daysOfWeekHighlighted: "0",
        todayHighlight: true
    });

    //sort
    $('#sortUp').click(function () {
        filter("desc");
    });

    $('#sortDown').click(function () {
        filter("asc");
    });

    // filter
    $('#filter').click(function () {
        filter("none");
    });

    $('#reset').click(function () {
        window.location.href = "http://localhost:8080/PocetniREST/html/hostActiveApartments.html";
    });

    // delete
    $('#delete').click(function(){
        alert('uso');
    });

    

});

function filter(sort) {
    let city = $('#city').val();
    let rating = "";
    if ($('#star5').hasClass('checked')) {
        rating = 5;
    }
    else if ($('#star4').hasClass('checked')) {
        rating = 4;
    }
    else if ($('#star3').hasClass('checked')) {
        rating = 3;
    }
    else if ($('#star2').hasClass('checked')) {
        rating = 2;
    }
    else if ($('#star1').hasClass('checked')) {
        rating = 1;
    }
    else {
        rating = 1;
    }

    let type = "";
    if ($('#room').is(':checked')) {
        type = "ROOM";
    }
    if ($('#apartment').is(':checked')) {
        type = "APARTMENT";
    }

    let rooms = [];
    if ($('#roomCheck1').is(':checked')) {
        rooms.push(1);
    }
    if ($('#roomCheck2').is(':checked')) {
        rooms.push(2);
    }
    if ($('#roomCheck3').is(':checked')) {
        rooms.push(3);
    }
    if ($('#roomCheck4').is(':checked')) {
        rooms.push(4);
    }
    if ($('#roomCheck5').is(':checked')) {
        rooms.push(5);
    }

    let guests = [];
    if ($('#guestCheck1').is(':checked')) {
        guests.push(1);
    }
    if ($('#guestCheck2').is(':checked')) {
        guests.push(2);
    }
    if ($('#guestCheck3').is(':checked')) {
        guests.push(3);
    }
    if ($('#guestCheck4').is(':checked')) {
        guests.push(4);
    }
    if ($('#guestCheck5').is(':checked')) {
        guests.push(5);
    }

    let minPrice = $('#minPrice').val() || "-1";
    let maxPrice = $('#maxPrice').val() || "-1";

    let amenities = [];
    if ($('#amenitiesCheck1').is(':checked')) {
        amenities.push("Parking");
    }
    if ($('#amenitiesCheck2').is(':checked')) {
        amenities.push("Kitchen");
    }
    if ($('#amenitiesCheck3').is(':checked')) {
        amenities.push("Dishwasher");
    }
    if ($('#amenitiesCheck4').is(':checked')) {
        amenities.push("Balcony");
    }
    if ($('#amenitiesCheck5').is(':checked')) {
        amenities.push("Air conditioning");
    }
    if ($('#amenitiesCheck6').is(':checked')) {
        amenities.push("Wifi");
    }
    if ($('#amenitiesCheck7').is(':checked')) {
        amenities.push("Pool");
    }
    if ($('#amenitiesCheck8').is(':checked')) {
        amenities.push("Spa");
    }
    if ($('#amenitiesCheck9').is(':checked')) {
        amenities.push("Fireplace");
    }

    let startDate = $('#startDate').val();
    let endDate = $('#endDate').val();

    $.ajax({
        url: '/PocetniREST/rest/apartments/filter/none?sort=' + sort,
        type: 'POST',
        data: JSON.stringify({ city: city, type: type, numberOfRooms: rooms, numberOfGuests: guests, minPrice: minPrice, maxPrice: maxPrice, rating: rating, amenities: amenities, startDate: startDate, endDate: endDate }),
        contentType: 'application/json',
        success: function (filteredApartments) {
            loadApartments(filteredApartments);
        }
    });
};

function addStar(num) {
    for (let i = 2; i <= 5; i++) {
        $('#star' + i).removeClass('checked');
    }

    while (num > 0) {
        $('#star' + num).addClass('checked');
        num--;
    }
};

function ratingStar(id, num) {
    for (let i = 1; i <= num; i++) {
        $('#' + id + i).addClass('checked');
    }
};

function loadApartments(apartments) {

    $('#apartmentsList').html(createApartments(apartments));
    for (var i in apartments) {
        if (parseInt(apartments[i].rating) > 4.5) {
            ratingStar(apartments[i].id, 5);
        }
        else if (parseInt(apartments[i].rating) > 3.5) {
            ratingStar(apartments[i].id, 4);
        }
        else if (parseInt(apartments[i].rating) > 2.5) {
            ratingStar(apartments[i].id, 3);
        }
        else if (parseInt(apartments[i].rating) > 1.5) {
            ratingStar(apartments[i].id, 2);
        }
        else if (parseInt(apartments[i].rating) > 0.5) {
            ratingStar(apartments[i].id, 1);
        }
        else {
            // nema recenzija
        }
    }

    // delete
    apartments.forEach(apartment => {

        $('#modal' + apartment.id).on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            name = button.data('name');
            id = button.data('id');
            var modal = $(this);
            modal.find('#msg' + apartment.id).text('Are you sure you want to delete ' + name + ' apartment?');     
            
            $('#delete' + apartment.id).click(function () {
                $('#modal' + apartment.id).modal('hide');
                $.ajax({
                    url: '/PocetniREST/rest/apartments/' + id,
                    type: 'DELETE'
                });
                
            });
        });
        
    });

    // slike
    apartments.forEach(apartment => {
        $.get({
            url: '/PocetniREST/rest/images/apartment/' + apartment.id,
            success: (images) => {
                if (images.length > 0) {
                    var img = ".." + images[0].imageURL;
                    $('#img' + apartment.id).attr("src", img+'?rand='+new Date());
                }
            }
        });
    });

    // update
    apartments.forEach(apartment => {
        $('#update' + apartment.id).click(function(){
            window.location.href = "http://localhost:8080/PocetniREST/html/newApartment.html?id=" + apartment.id;
        });
    });

     // amenities za filter
    $.get({
        url: '/PocetniREST/rest/amenities',
        success: function(amenities){
            let i = true;
            $('#amenities1').empty();
            $('#amenities2').empty();
            amenities.forEach( amenity => {
                let amenityHtml = '<div class="custom-control custom-checkbox">'+
                                    '<input type="checkbox" class="custom-control-input" id="amenitiesCheck'+amenity.id+'">'+
                                    '<label class="custom-control-label" for="amenitiesCheck'+amenity.id+'">'+amenity.name+'</label>'+
                                '</div>';
                if(i){
                    $('#amenities1').append(amenityHtml);
                    i = false;
                }
                else{
                    $('#amenities2').append(amenityHtml);
                    i = true;
                }
            });
        }
    });

};

function createApartments(apartments) {
    let html = "";
    for (var i in apartments) {

        html += '<div class="row mt-0 mb-5 ml-5 mr-5 p-2 border" >' +
                '<div class="col-lg-4 col-md-12 col-sm-12 p-0">' +
                    '<img src="../img/Capture.jpg" alt="image" class="img-thumbnail" id="img' + apartments[i].id + '">' +
                '</div>' +
                '<div class="col-lg-4 col-md-12 col-sm-12 text-left pl-3" id="leftPanel">' +
                    '<div>' +
                        '<div>' + // Naziv apartmana
                            '<p class="bg-info text-white h5 mb-1 p-1 rounded">' + apartments[i].name + '</p>' +
                        '</div>' +
                        '<div>' + // Naziv grada
                            '<p class="bg-light p-1 rounded"><i class="fa fa-map-marker"></i> ' + apartments[i].city + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div>' +
                        '<div id="roomsGuests">' + // broj gostiju i broj soba
                            '<p class="bg-light p-1 rounded"><i class="fa fa-male"></i> ' + apartments[i].numberOfGuests + '</p>' +
                            '<p class="bg-light p-1 rounded"><i class="fa fa-key"></i> ' + apartments[i].numberOfRooms + '</p>' +
                        '</div>' +
                        '<div>' + // tip apartmana i cena
                            '<p class="bg-light p-1 rounded">' + apartments[i].type + '</p>' +
                            '<p class="bg-light p-1 rounded"><i class="fa fa-dollar"></i> ' + apartments[i].price + '</p>' +
                        '</div>' +
                    '</div>' +
                '</div>' +
                '<div class="col-lg-4 col-md-12 col-sm-12 text-right" id="rightPanel">' +
                    '<div class="row">' +
                        '<div class="col">' + // prosecna ocena
                            '<div class="row">'+
                                '<div class="col text-right">'+
                                    '<p class="mb-xl-1 p-1">' +
                                        '<span class="fa fa-star" id="' + apartments[i].id + '1"></span>' +
                                        '<span class="fa fa-star" id="' + apartments[i].id + '2"></span>' +
                                        '<span class="fa fa-star" id="' + apartments[i].id + '3"></span>' +
                                        '<span class="fa fa-star" id="' + apartments[i].id + '4"></span>' +
                                        '<span class="fa fa-star" id="' + apartments[i].id + '5"></span>' +
                                    '</p>' +
                                '</div>'+
                            '</div>'+
                            '<div class="row mb-2">' + // broj recenzija
                                '<div class="col text-right">'+
                                    '<p class="bg-light p-1 rounded">' + apartments[i].reviews;
                    if (apartments[i].reviews == 1) {
                        html += ' review</p>';
                    }
                    else {
                        html += ' reviews</p>';
                    }
                    html +=     '</div>' +
                            '</div>' +
                            '<div class="row">' +
                                '<div class="col text-right">'+
                                    '<button id="update' + apartments[i].id + '" type="button" class="btn btn-outline-warning mr-1">Update</button>' +
                                    '<button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#modal' + apartments[i].id + '" data-name="'+ apartments[i].name +'" data-id="'+ apartments[i].id +'">Delete</button>' +

                                    // Modal
                                    '<div class="modal fade" id="modal' + apartments[i].id + '" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">' +
                                        '<div class="modal-dialog modal-dialog-centered" role="document">' +
                                            '<div class="modal-content">' +
                                            '<div class="modal-header">' +
                                                '<h5 class="modal-title" id="exampleModalLongTitle">Delete</h5>' +
                                                '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                                                '<span aria-hidden="true">&times;</span>' +
                                                '</button>' +
                                            '</div>' +
                                            '<div class="modal-body">' +
                                                '<p id="msg'+ apartments[i].id +'"></p>'+
                                            '</div>' +
                                            '<div class="modal-footer">' +
                                                '<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>' +
                                                '<button id="delete'+ apartments[i].id +'" type="button" class="btn btn-info">Delete</button>' +
                                            '</div>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +

                                '</div>'+
                            '</div>' +
                            '<div class="row mt-2">' +
                                '<div class="col text-right">'+
                                '<div class="d-flex flex-column">'+
                                    '<a href="http://localhost:8080/PocetniREST/html/apartmentProfile.html?id=' + apartments[i].id + '" class="btn btn-outline-info">Details</a>' +
                                '</div>'+
                                '</div>'+
                            '</div>' +
                        '</div>'+
                    '</div>'+
                '</div>' +
            '</div>';
    }

    return html;
};