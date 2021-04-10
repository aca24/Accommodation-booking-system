$(document).ready(function(){

    let queryParam = new URLSearchParams (window.location.search);
    let longitude = queryParam.get('lon');
    let latitude = queryParam.get('lat');

    map = new OpenLayers.Map("mapdiv");
      map.addLayer(new OpenLayers.Layer.OSM());
  
      var lonLat = new OpenLayers.LonLat( longitude, latitude)
            .transform(
              new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
              map.getProjectionObject() // to Spherical Mercator Projection
            );
            
      var zoom=16;
  
      var markers = new OpenLayers.Layer.Markers( "Markers" );
      map.addLayer(markers);
      
      markers.addMarker(new OpenLayers.Marker(lonLat));
      
      map.setCenter (lonLat, zoom);
});