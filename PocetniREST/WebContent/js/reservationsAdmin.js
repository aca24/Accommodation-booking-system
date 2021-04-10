$(document).ready(function() {
    $.get({
        url: '/PocetniREST/rest/reservations/getAll',
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
        url: '/PocetniREST/rest/reservations/sorterAdmin/' + sort,
        type: 'POST',
        contentType: 'application/json',
        success: function (sortedReservations) {
            loadReservations(sortedReservations);
        }
    });
};

function loadReservations(reservations) {

    $('#reservationsList').html(createReservations(reservations));
}

function createReservations(reservations) {
    let html = "";
    html += '<div class="row mt-2 mb-2 ml-2 mr-2">' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Apartment</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Host</h4>' +
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
                        '<h4>Reservation status</h4>' +
                    '</div>' +
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
                            '<p class="bg-light p-1 rounded">' + reservation.host + '</p>' +
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
                            '<p class="bg-light p-1 rounded" id="status">' + reservation.status + '</p>' +
                        '</div>' +
                    '</div>' +
                '</div>';
    }
    return html;
}