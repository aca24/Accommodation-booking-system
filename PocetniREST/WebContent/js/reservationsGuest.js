$(document).ready(function() {
    $.get({
        url: '/PocetniREST/rest/reservations/getReservationsGuest',
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
        url: '/PocetniREST/rest/reservations/sorterGuest/' + sort,
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

        $('#modal' + reservation.id).on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            id = button.data('id');
            var modal = $(this);
            modal.find('#msg' + reservation.id).text('Are you sure you want to cancel ' + reservation.apartmentName + ' reservation?');     
            
            $('#cancel' + reservation.id).click(function () {

                $.ajax({
                    url: '/PocetniREST/rest/reservations/cancel/' + id,
                    type: 'PUT',
                    success: function(reservation) {
                        $('#status' + reservation.id).text(reservation.status);
                        $('#modal' + reservation.id).modal('hide');
                    },
                    error: function(errorMessage) {
                        $('#cancelError' + reservation.id).text(errorMessage);
                    }

                });
                
            });
        });
        
    });
}

function createReservations(reservations) {
    let html = "";
    html += '<div class="row mt-2 mb-2 ml-2 mr-2">' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Name</h4>' +
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
                        '<h4>Reservation status</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                '</div>' +
            '</div>';

    for (var reservation of reservations) {
        
        html += '<div class="row mt-2 mb-2 ml-2 mr-2 border">' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-info text-white h5 p-1 rounded">' + reservation.apartmentName + '</p>' +
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
                            '<p class="bg-light p-1 rounded" id="status' + reservation.id +'">' + reservation.status + '</p>' +
                        '</div>' +
                    '</div>';

                if (reservation.status == "CREATED" || reservation.status == "ACCEPTED") {
                    html += '<div class="col">' +
                        '<div>' +
                            '<button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#modal' + reservation.id + '" data-name="'+ reservation.apartmentName +'" data-id="'+ reservation.id +'">Cancel</button>' +
                            '<div class="modal fade" id="modal' + reservation.id + '" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">' +
                                '<div class="modal-dialog modal-dialog-centered" role="document">' +
                                    '<div class="modal-content">' +
                                        '<div class="modal-header">' +
                                            '<h5 class="modal-title" id="exampleModalLongTitle">Cancel</h5>' +
                                            '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                                            '<span aria-hidden="true">&times;</span>' +
                                            '</button>' +
                                        '</div>' +
                                        '<div class="modal-body">' +
                                            '<p id="msg'+ reservation.id +'"></p>'+
                                        '</div>' +
                                        '<div class="modal-footer">' +
                                            '<p id="cancelError'+ reservation.id +'"></p>' +
                                            '<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>' +
                                            '<button id="cancel'+ reservation.id +'" type="button" class="btn btn-info">Cancel</button>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +

                        '</div>' +
                    '</div>';
                } else {
                    html += '<div class="col">' +
                    '</div>';
                }
                html += '</div>';
    }
    return html;
}