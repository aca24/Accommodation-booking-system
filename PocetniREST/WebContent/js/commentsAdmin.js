$(document).ready(function() {
    $.get({
        url: '/PocetniREST/rest/comments/getAll',
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
                            '<input type="checkbox" id="approved' + comment.id + '" onclick="return false;"/>' +
                        '</div>' +
                    '</div>' +
                '</div>';
    }
    return html;
}