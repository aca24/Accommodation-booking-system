$(document).ready(function() {
    $.get({
        url: '/PocetniREST/rest/comments/getMyComments',
        success: function(comments) {
            loadComments(comments);

            for (var comment of comments) {
                if (comment.approved) {
                    $('#approved' + comment.id).prop('checked', true);
                } else {
                    $('#approved' + comment.id).prop('checked', false);
                }
            }
        }
    });
});

function loadComments(comments) {

    $('#commentsList').html(createComments(comments));

    comments.forEach(comment => {
        
        $("#approved" + comment.id).change(function() {
            if(this.checked) {
                $.ajax({
                    url: '/PocetniREST/rest/comments/approve/' + comment.id,
                    type: 'PUT',
                    success: function() {
                        $("#error"+comment.id).text("You successfully changed comment visibility.");
                        $("#error"+comment.id).removeClass('text-danger');
                        $("#error"+comment.id).addClass('text-info');
                        $("#error"+comment.id).show().delay(1000).fadeOut(300)
                    }, error: function() {
                        $("#error"+comment.id).text("Error, try again later.");
                        $("#error"+comment.id).removeClass('text-info');
                        $("#error"+comment.id).addClass('text-danger');
                        $("#error"+comment.id).show().delay(1000).fadeOut(300)
                    }
                });
            }
            else {
                $.ajax({
                    url: '/PocetniREST/rest/comments/disprove/' + comment.id,
                    type: 'PUT',
                    success: function() {
                        $("#error"+comment.id).text("You successfully changed comment visibility.");
                        $("#error"+comment.id).removeClass('text-danger');
                        $("#error"+comment.id).addClass('text-info');
                        $("#error"+comment.id).show().delay(1000).fadeOut(300)
                    }, error: function() {
                        $("#error"+comment.id).text("Error, try again later.");
                        $("#error"+comment.id).removeClass('text-info');
                        $("#error"+comment.id).addClass('text-danger');
                        $("#error"+comment.id).show().delay(1000).fadeOut(300)
                    }
                });
            }
        });
    });
}

function createComments(comments) {
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
                        '<h4>Comment</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Rating</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="col">' +
                    '<div>' +
                        '<h4>Approved</h4>' +
                    '</div>' +
                '</div>' +
            '</div>';
    
    for (var comment of comments) {

        html += '<div class="row mt-2 mb-2 ml-2 mr-2 border">' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-info text-white h5 p-1 rounded">' + comment.apartment + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + comment.guest + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + comment.text + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<p class="bg-light p-1 rounded">' + comment.rating + '</p>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col">' +
                        '<div>' +
                            '<input type="checkbox" id="approved' + comment.id + '">' +
                            '<br><p id = "error' + comment.id + '"></p>' +
                        '</div>' +
                    '</div>' +
                '</div>';
    }
    return html;
}