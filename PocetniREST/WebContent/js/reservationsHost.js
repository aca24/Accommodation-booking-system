$(document).ready(function() {
    $.get({
        url: '/PocetniREST/rest/reservations/getReservationsHost',
        success: function(reservations) {
            loadReservations(reservations);
        }
    });

    $('#sortUp').click(function () {
        filter("desc");
    });

    $('#sortDown').click(function () {
        filter("asc");
    });
});

function filter(sort) {
    $.ajax({
        url: '/PocetniREST/rest/reservations/sorterHost/' + sort,
        type: 'POST',
        contentType: 'application/json',
        success: function (sortedReservations) {
            loadReservations(sortedReservations);
        }
    });
};


function loadReservations(reservations) {

    $('#reservationsList').html(createReservations(reservations));

    reservations.forEach(reservation => {

        $('#rejectModal' + reservation.id).on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            id = button.data('id');
            var modal = $(this);
            modal.find('#rejectMsg' + reservation.id).text('Are you sure you want to reject ' + reservation.apartmentName + ' reservation?');     
            
            $('#reject' + reservation.id).click(function () {

                $.ajax({
                    url: '/PocetniREST/rest/reservations/reject/' + id,
                    type: 'PUT',
                    success: function(reservation) {
                        $('#status' + reservation.id).text(reservation.status);
                        $('#rejectModal' + reservation.id).modal('hide');
                    },
                    error: function(errorMessage) {
                        $('#rejectError' + reservation.id).text(errorMessage);
                    }
                });
                
            });
        });       
    });

    reservations.forEach(reservation => {

        $('#acceptModal' + reservation.id).on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            id = button.data('id');
            var modal = $(this);
            modal.find('#acceptMsg' + reservation.id).text('Are you sure you want to accept ' + reservation.apartmentName + ' reservation?');     
            
            $('#accept' + reservation.id).click(function () {

                $.ajax({
                    url: '/PocetniREST/rest/reservations/accept/' + id,
                    type: 'PUT',
                    success: function(reservation) {
                        $('#status' + reservation.id).text(reservation.status);
                        $('#acceptModal' + reservation.id).modal('hide');
                    },
                    error: function(errorMessage) {
                        $('#acceptError' + reservation.id).text(errorMessage);
                    }
                });
                
            });
        });     
    });

    reservations.forEach(reservation => {

        $('#finishModal' + reservation.id).on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            id = button.data('id');
            var modal = $(this);
            modal.find('#finishMsg' + reservation.id).text('Are you sure you want to finish ' + reservation.apartmentName + ' reservation?');     
            
            $('#finish' + reservation.id).click(function () {

                $.ajax({
                    url: '/PocetniREST/rest/reservations/finish/' + id,
                    type: 'PUT',
                    success: function(reservation) {
                        $('#status' + reservation.id).text(reservation.status);
                        $('#finishModal' + reservation.id).modal('hide');
                    },
                    error: function(errorMessage) {
                        $('#finishError' + reservation.id).text(errorMessage);
                    }
                });
                
            });
        });     
    });
}

function createReservations(reservations) {

    var q = new Date();
    var m = q.getMonth();
    var d = q.getDay();
    var y = q.getFullYear();
    
    var today = new Date(y,m,d);

    let html = "";
    html += '<div class="row mt-2 mb-2 ml-2 mr-2">' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Apartment</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Guest</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Date</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Number of days</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Price</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Message</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Reservation status</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                '</div>' +
                '<div class="col">' +
                '</div>' +
                '<div class="col">' +
                '</div>' +
            '</div>';

    for (var reservation of reservations) {

        var resDay = reservation.date;
        var d = resDay.split('-');
        var reservedDay = new Date (d[2], d[1] - 1,d[0]);
        
        html += '<div class="row mt-2 mb-2 ml-2 mr-2 border">' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-info text-white h5 p-1 rounded">' + reservation.apartmentName + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + reservation.guest + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + reservation.date + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + reservation.numberOfStays + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + reservation.price + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + reservation.message + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded" id="status' + reservation.id + '">' + reservation.status + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">';

                if (reservation.status == "CREATED" || reservation.status == "ACCEPTED") {
                    html += '<div>' +
                                '<button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#rejectModal' + reservation.id + '" data-name="'+ reservation.apartmentName +'" data-id="'+ reservation.id +'">Reject</button>' +
                                '<div class="modal fade" id="rejectModal' + reservation.id + '" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">' +
                                    '<div class="modal-dialog modal-dialog-centered" role="document">' +
                                        '<div class="modal-content">' +
                                            '<div class="modal-header">' +
                                                '<h5 class="modal-title" id="exampleModalLongTitle">Reject</h5>' +
                                                '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                                                '<span aria-hidden="true">&times;</span>' +
                                                '</button>' +
                                            '</div>' +
                                            '<div class="modal-body">' +
                                                '<p id="rejectMsg'+ reservation.id +'"></p>'+
                                            '</div>' +
                                            '<div class="modal-footer">' +
                                                '<p id="rejectError'+ reservation.id +'"></p>' +
                                                '<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>' +
                                                '<button id="reject'+ reservation.id +'" type="button" class="btn btn-info">Reject</button>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>';
                }
                html += '</div>' +
                        '<div class="col">';

                if (reservation.status == "CREATED") {
                    html += '<div>' +
                                '<button type="button" class="btn btn-outline-success" data-toggle="modal" data-target="#acceptModal' + reservation.id + '" data-name="'+ reservation.apartmentName +'" data-id="'+ reservation.id +'">Accept</button>' +
                                '<div class="modal fade" id="acceptModal' + reservation.id + '" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">' +
                                    '<div class="modal-dialog modal-dialog-centered" role="document">' +
                                        '<div class="modal-content">' +
                                            '<div class="modal-header">' +
                                                '<h5 class="modal-title" id="exampleModalLongTitle">Accept</h5>' +
                                                '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                                                '<span aria-hidden="true">&times;</span>' +
                                                '</button>' +
                                            '</div>' +
                                            '<div class="modal-body">' +
                                                '<p id="acceptMsg'+ reservation.id +'"></p>'+
                                            '</div>' +
                                            '<div class="modal-footer">' +
                                                '<p id="acceptError'+ reservation.id +'"></p>' +
                                                '<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>' +
                                                '<button id="accept'+ reservation.id +'" type="button" class="btn btn-info">Accept</button>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>';
                }
                html += '</div>' +
                        '<div class="col">';

                if (today > reservedDay && (reservation.status == "CREATED" || reservation.status == "ACCEPTED")) {
                    html += '<div>' +
                                '<button type="button" class="btn btn-outline-info" data-toggle="modal" data-target="#finishModal' + reservation.id + '" data-name="'+ reservation.apartmentName +'" data-id="'+ reservation.id +'">Finish</button>' +
                                '<div class="modal fade" id="finishModal' + reservation.id + '" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">' +
                                    '<div class="modal-dialog modal-dialog-centered" role="document">' +
                                        '<div class="modal-content">' +
                                            '<div class="modal-header">' +
                                                '<h5 class="modal-title" id="exampleModalLongTitle">Finish</h5>' +
                                                '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                                                '<span aria-hidden="true">&times;</span>' +
                                                '</button>' +
                                            '</div>' +
                                            '<div class="modal-body">' +
                                                '<p id="finishMsg'+ reservation.id +'"></p>'+
                                            '</div>' +
                                            '<div class="modal-footer">' +
                                                '<p id="finishError'+ reservation.id +'"></p>' +
                                                '<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>' +
                                                '<button id="finish'+ reservation.id +'" type="button" class="btn btn-info">Finish</button>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>';
                }
                html += '</div>' +
                    '</div>';
    }
    return html;
}