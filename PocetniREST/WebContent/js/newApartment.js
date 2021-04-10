$(document).ready(function () {

  let update = false;
  let status = 'INACTIVE';
  let id = -1;
   // load amenities
   $.get({
    url: '/PocetniREST/rest/amenities',
    cache: false,
    success: function(amenities){
      loadAmenities(amenities);

      var urlParams = new URLSearchParams(window.location.search);
      // fill data
      if(urlParams.has('id')){
        update = true;
        id = urlParams.get('id');
        $.get({
          url: '/PocetniREST/rest/apartments/update/' + id,
          success: function (apartment) {

              status = apartment.status;
              console.log(apartment.status);
              $('#name').val(apartment.name);
              if(apartment.type == 'ROOM'){
                $('#room').prop('checked', true);
              }
              else{
                $('#apartment').prop('checked', true);
              }
              $('#city').val(apartment.city);
              $('#street').val(apartment.street);
              $('#number').val(apartment.number);
              $('#postalCode').val(apartment.postalCode);
              $('#price').val(apartment.price);
              $('#rooms').val(apartment.numberOfRooms);
              $('#guests').val(apartment.numberOfGuests);
              $('#checkIn').attr("value", apartment.checkInTime + ':00');
              $('#checkOut').attr("value", apartment.checkOutTime + ':00');

              // amanities
              let amenities = apartment.amenities;
              for(var a = 0; a < amenities.length; a++){
                $('#amenitiesCheck' + amenities[a]).prop('checked', true);
              }
              // dates for rent
              var dateArr = apartment.dates;
              var arr = new Array();
              for (var j = 0; j < dateArr.length; j++) {
                      var dateParts = dateArr[j].split("-");
                      let d = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);
                      arr.push(d);
              }

              $("#datepicker1").datepicker("setDates", arr);

              // ucitavanje slika
              $.get({
                url: '/PocetniREST/rest/images/apartment/' + id,
                success: (images)=>{
                  console.log(images);
                  let length = images.length;

                  if(length > 0){
                      images.forEach(img => {
                          let url = ".." + img.imageURL;
                          let i = img.imageURL.split('/');
                          i = i[3].split('.');
                          i = i[0];
                          let inputImg = $('#thumbnail' + i);
                          inputImg.prop('hidden', false);
                          inputImg.attr('src', url);

                          $('#remove'+ i).prop('hidden', false);
                      });                
                  } 
                }
              });
          }
        });
      }
    }
  });
  

  let apartmentId;
  

  // images set filename
  $(".custom-file-input").on("change", function() {
    var fileName = $(this).val().split("\\").pop();
    $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    let imgName = $(this).attr('name');

    let inputImg = $('#thumbnail' + imgName);
    //inputImg.prop('hidden', true);
    inputImg.attr('src', ''); 

    

    let file = this.files[0];
    var data = new FormData();
    data.append('file', file);
    
    $.ajax({
      url: '/PocetniREST/rest/images/upload/' + imgName + "/" + apartmentId + "?update=" + update,
      data: data,
      type: 'POST',
      processData: false,
      contentType: false,
      success: function(data){
        let url = '../img/apartment' + apartmentId + '/' + imgName + '.jpg';
        let inputImg = $('#thumbnail' + imgName);
        inputImg.prop('hidden', false);
        inputImg.attr('src', url+'?rand='+new Date());
        
        $('#remove'+ imgName).prop('hidden', false);
        
      },
      error: function(){
      }
    });
  });

  $('#remove1').click(function(){
    removeImage(1, apartmentId);
  });

  $('#remove2').click(function(){
    removeImage(2, apartmentId);
  });

  $('#remove3').click(function(){
    removeImage(3, apartmentId);
  });

  $('#remove4').click(function(){
    removeImage(4, apartmentId);
  });

  $('#remove5').click(function(){
    removeImage(5, apartmentId);
  });

  //#region Submit 
  $('form').submit(function(event){
    event.preventDefault();

    let data = $( this ).serializeArray();
    let name = data[0].value;
    let type = data[1].value;
    let city = data[2].value;
    let street = data[3].value;
    let number = data[4].value;
    let postalCode = data[5].value;
    let price = data[6].value;
    let rooms = data[7].value;
    let guests = data[8].value;
    let checkIn = data[9].value;
    let checkOut = data[10].value;
    let amenities = new Array();
    for(var i = 11; i < data.length; i++){
      amenities.push(data[i].name);
    }
    let dates = $('#datepicker1').datepicker('getFormattedDate');
    let startDate = $('#startDate').val();
    let endDate = $('#endDate').val();
    let longitude;
    let latitude;

    // get coordinates
    $.ajax({
        url: 'https://api.nettoolkit.com/v1/geo/geocodes?address='+ city +' '+ street +' '+ number,
        headers: {
            'X-NTK-KEY': 'test_gBlUuFoYDpVJS63R54cAMGHstwodiUwUtBBaGNRH'
        },
        type: 'GET',
        success: function(data){
            longitude = data.results[0].latitude;
            latitude = data.results[0].longitude;        

            $.ajax({
                url: '/PocetniREST/rest/apartments/create?id=' + id,
                type: 'POST',
                data: JSON.stringify({name: name, type: type, status: status, city: city, street: street, 
                  number: number, postalCode: postalCode, longitude: longitude, latitude: latitude, 
                  price: price, numberOfRooms: rooms, numberOfGuests: guests, 
                  checkInTime: checkIn, checkOutTime: checkOut, amenities: amenities, dates: dates, startDate: startDate, endDate:endDate}),
                contentType: 'application/json',
                success: function(id){
                  apartmentId = id;
                  valid = true;
                  nextStep($('#create')[0]);
                  $('#msg4').removeClass('is-invalid');
                  $('#msg4').addClass('text-info');
                  $('#msg4').text("New apartment is created successfully.");
                  $('#msg4').show().delay(3000).fadeOut(1000);
                },
                error: function(jqxhr){
                  $('#msg3').removeClass('text-info');
                  $('#msg3').addClass('is-invalid');
                  $('#msg3').text(jqxhr.responseText);
                  $('#msg3').show().delay(3000).fadeOut(1000);
                }
            });
        }
    });
  });
  //#endregion

  $('#finish').click(function(){
    window.location.href = "http://localhost:8080/PocetniREST/html/apartmentProfile.html?id="+ apartmentId;
  });

  //#region Validation
  let name;
  let type;
  let city;
  let street;
  let number;
  let postalCode;

  let price;
  let rooms;
  let guests;

  let valid = false;
  let prolaz = 1;
  let previous = false;
  // validation step 1
  $('#btnNext1').click(function(){

    $('#msg1').text('');
    $('#name').removeClass('is-invalid');
    $('#city').removeClass('is-invalid');
    $('#street').removeClass('is-invalid');
    $('#number').removeClass('is-invalid');
    $('#postalCode').removeClass('is-invalid');
    $('#room').removeClass('is-invalid');
    $('#apartment').removeClass('is-invalid');

    name = $('#name').val();
    type = "";
    if ($('#room').is(':checked')) {
        type = "ROOM";
    }
    if ($('#apartment').is(':checked')) {
        type = "APARTMENT";
    }
    city = $('#city').val();
    street = $('#street').val();
    number = $('#number').val();
    postalCode = $('#postalCode').val();

    if(name == ""){
      $('#name').addClass('is-invalid');
      $('#msg1').text('Please, fill out name field.');
    }
    else if(type == ""){
      $('#room').addClass('is-invalid');
      $('#apartment').addClass('is-invalid');
      $('#msg1').text('Please, choose the apartment type.');
    }
    else if(city == ""){
      $('#city').addClass('is-invalid');
      $('#msg1').text('Please, fill out city field.');
    }
    else if(street == ""){
      $('#street').addClass('is-invalid');
      $('#msg1').text('Please, fill out street field.');
    }
    else if(number == ""){
      $('#number').addClass('is-invalid');
      $('#msg1').text('Please, fill out number field.');
    }
    else if(postalCode == ""){
      $('#postalCode').addClass('is-invalid');
      $('#msg1').text('Please, fill out postal code field.');
    }
    else{
      valid = true;
      nextStep(this);
    }

  });

  // validation step 2
  $('#btnNext2').click(function(){

    $('#msg2').text('');
    $('#price').removeClass('is-invalid');
    $('#rooms').removeClass('is-invalid');
    $('#guests').removeClass('is-invalid');

    price = $('#price').val();
    rooms = $('#rooms').val();
    guests = $('#guests').val();

    if(price == ""){
      $('#price').addClass('is-invalid');
      $('#msg2').text('Please, fill out price field.');
    }
    else if(rooms == ""){
      $('#rooms').addClass('is-invalid');
      $('#msg2').text('Please, fill out number of rooms field.');
    }
    else if(guests == ""){
      $('#guests').addClass('is-invalid');
      $('#msg2').text('Please, fill out number of guests field.');
    }
    else{
      valid = true;
      nextStep(this);
    }

  });
  
  //previous button
  $('.previousBtn').click(function(){ 
    previous = true;
    previousStep(this);
  });
  //#endregion

  //#region multi step form 
  var navListItems = $('div.setup-panel div a'),
            allWells = $('.setup-content');

  allWells.hide();

  navListItems.click(function (e) {
    e.preventDefault();
    if(valid || prolaz == 1 || previous){
      valid = false;
      prolaz = 0;
      previous = false;
      var $target = $($(this).attr('href')),
                $item = $(this);

      navListItems.removeClass('btn-info').addClass('btn-default');
      $item.addClass('btn-info');
      allWells.hide();
      $target.show();
      $target.find('input:eq(0)').focus();
        
    }
  });

  $('div.setup-panel div a.btn-info').trigger('click');
  //#endregion

  //#region Datepicker, Clockpicker
    // datepicker
    $('#datepicker1').datepicker({
        format: "dd-mm-yyyy",
        weekStart: 1,
        startDate: "today",
        maxViewMode: 2,
        multidate: true,
        clearBtn: true,
        daysOfWeekHighlighted: "0",
        todayHighlight: true
    });

    $('#datepicker2').datepicker({
      format: "dd-mm-yyyy",
      weekStart: 1,
      startDate: "today",
      maxViewMode: 2,
      orientation: "bottom auto",
      daysOfWeekHighlighted: "0",
      todayHighlight: true
  });

    var input = $('#input-a');
    var choices = ["00"];
    var hourSelected = false;
    var selectedHour = "";
    var hiddenTicksDisabled = true;

    // clockpicker
    $('#clockpicker1').clockpicker({
        placement: 'top',
        align: 'left',
        donetext: 'Done',
        afterShow: function() {  // Remove all unwanted minute display.
            $(".clockpicker-minutes").find(".clockpicker-tick").filter(function(index,element){
              return !($.inArray($(element).text(), choices)!=-1)
            }).remove();
          }
    });

    // clockpicker
    $('#clockpicker2').clockpicker({
        placement: 'top',
        align: 'left',
        donetext: 'Done',
        afterShow: function() {  // Remove all unwanted minute display.
            $(".clockpicker-minutes").find(".clockpicker-tick").filter(function(index,element){
              return !($.inArray($(element).text(), choices)!=-1)
            }).remove();
          }
    });

    
// Handler to re-open the picker on "wrong" click.
$(document).on("click",".clockpicker-plate",function(e){

    if(hiddenTicksDisabled && hourSelected){
      console.log("NOT a valid minute click!");
  
      // Keep the hour value but clear the input field and reopen the picker at minutes
      setTimeout(function(){
        input.val(selectedHour);
        input.clockpicker('show').clockpicker('toggleView', 'minutes');
        input.val("");
      },400);
  
    }
  });
  
  // Handlers to toggle the "hiddenTicksDisabled"
  $(document).on("mouseenter",".clockpicker-minutes>.clockpicker-tick",function(e){
    hiddenTicksDisabled = false;
  });
  
  $(document).on("mouseleave",".clockpicker-minutes>.clockpicker-tick",function(e){
    setTimeout(function(){
      hiddenTicksDisabled = true;
    },100);
  });
  //#endregion
});

function removeImage(imgName, id){

  $.ajax({
    url: '/PocetniREST/rest/images/' + imgName + '/' + id,
    type: 'DELETE',
    success: function(){
      let inputImg = $('#thumbnail' + imgName);
      inputImg.prop('hidden', true);
      inputImg.attr('src', '');      
        
      $('#remove'+ imgName).prop('hidden', true);
    }
  });
}

function showImage(img) {
  let expandImg = $('#expandedImg');
  expandImg.attr('src', img.attr('src'));
};

function loadAmenities(amenities){

  let i = 1;
    amenities.forEach(amenity => {
        if(i == 4){
            i = 1;
        }
        $('#col'+i).append('<div class="custom-control custom-checkbox">'+
                              '<input type="checkbox" class="custom-control-input" name="'+ amenity.id +'" id="amenitiesCheck'+ amenity.id +'">'+
                              '<label class="custom-control-label" for="amenitiesCheck'+ amenity.id +'">'+ amenity.name +'</label>'+
                            '</div>');
        i++;
    });
};

function nextStep(that){
  var curStep = $(that).closest(".setup-content");
  var curStepBtn = curStep.attr("id");
  var nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().next().children("a");

  nextStepWizard.removeAttr('disabled').trigger('click');
};

function previousStep(that){
  var curStep = $(that).closest(".setup-content");
  var curStepBtn = curStep.attr("id");
  var prevStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().prev().children("a");

  prevStepWizard.trigger('click');
};