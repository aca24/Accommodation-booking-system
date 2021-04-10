$(document).ready(function(){
    $.get({
        url: '/PocetniREST/rest/amenities',
        success: function(data){
            var html = '';
            var br = 1;
            for(i in data){	
                html += '<tr class="row">' +
                        '<th scope="row" class="col-1">' + br + '</th>' +
                        '<td class="col-9">' + data[i].name + '</td>' +
                        '<td class="col-1"><button class="fa fa-remove btn" type="button" data-toggle="modal" data-target="#modal" id="amenity' + data[i].id +'" data-name="'+ data[i].name +'" data-id="'+ data[i].id +'"></button></td>' +	
                        '<td class="col-1"><a href="http://localhost:8080/PocetniREST/html/newAmenity.html?id='+ data[i].id +'" class="fa fa-edit btn" type="button"></a></td>' +
                        '</tr>';
                br++;
            }

            $('tbody').html(html);        
        }
    });

    $('#tableSearch').on('keyup', function() {
        var value = $(this).val().toLowerCase();
        $('tbody tr').filter(function() {
          $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    // new amenity
    $('#new').click(function(){
        window.location.href = "http://localhost:8080/PocetniREST/html/newAmenity.html";
    });

      // modal
    $('#modal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        name = button.data('name');
        id = button.data('id');
        var modal = $(this);
        modal.find('#msg').text('Are you sure you want to delete ' + name + ' amenity?');     
        
        $('#delete').click(function () {
            console.log(name);
            console.log(id);
            $('#modal').modal('hide');
            $( "#delete" ).off( "click");
            $.ajax({
                url: '/PocetniREST/rest/amenities/' + id,
                type: 'DELETE'
            });
            
        });
    });
});

