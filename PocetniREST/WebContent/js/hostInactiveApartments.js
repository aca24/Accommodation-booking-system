$(document).ready(function () {

    // ucitavanje apartmana
    $.get({
        url: '/PocetniREST/rest/apartments/hostApartmentsInactive',
        success: function (apartments) {
            loadApartments(apartments);
        }
    });

    // delete
    $('#delete').click(function(){
        alert('uso');
    });
});

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

    apartments.forEach(apartment => {
        $('#update' + apartment.id).click(function(){
            window.location.href = "http://localhost:8080/PocetniREST/html/newApartment.html?id=" + apartment.id;
        });
    });
};

function createApartments(apartments) {
    let html = "";
    for (var i in apartments) {

        html += 
            '<div class="row mt-0 mb-5 ml-5 mr-5 p-2 border" >' +
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
                '   <div>' +
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
                    html += '</div>' +
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
                                '   <div class="d-flex flex-column">'+
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