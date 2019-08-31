<%--
  Created by IntelliJ IDEA.
  User: Felix
  Date: 07.08.2019
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.5.1/dist/leaflet.css"
          integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
          crossorigin=""/>
    <!-- Make sure you put this AFTER Leaflet's CSS -->
    <script src="https://unpkg.com/leaflet@1.5.1/dist/leaflet.js"
            integrity="sha512-GffPMF3RvMeYyc1LWMHtK8EbPv0iNZ8/oTtHPx9/cc2ILxQ+u905qIwdpULaqDkyBKgOaB57QTMg7ztg8Jm2Og=="
            crossorigin=""></script>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <style>
        #germanymap {
            height: 90vh;
        }
    </style>
    <title>Routenplaner</title>
</head>
<body>
<div id="germanymap"></div>
<span id="importing">Importing graph data...</span>
<p>Click location: <span id="latitude">latitude</span> | <span id="longitude">longitude</span></p>
</body>

<script>
    var isProcessing = true;
    $.get("MapServlet", function () {
        document.getElementById("importing").innerHTML = "Import successful!"
        isProcessing = false;
    })


    var coords = [];
    var nextLoc = 0;
    var startLoc = {Double: latitude, Double: longitude};
    var endLoc = {Double: latitude, Double: longitude};
    var mymap = L.map("germanymap");
    var osmURL = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    var osmAttrib = 'Map data &copy <a href="https://openstreetmap.org">OpenStreetMap</a> contributors';
    var osm = new L.tileLayer(osmURL, {minZoom: 7, maxZoom: 19, attribution: osmAttrib});
    mymap.setView(new L.LatLng(53.60, 13.39), 9);
    osm.addTo(mymap)
    var startMarker = L.marker([0, 0], {opacity: 0}).addTo(mymap);
    var endMarker = L.marker([0, 0], {opacity: 0}).addTo(mymap);


    function onMapClick(e) {

        document.getElementById("latitude").textContent = e.latlng.lat;
        document.getElementById("longitude").textContent = e.latlng.lng;

        if (nextLoc == 0 && !isProcessing) {
            endLoc = {};
            startLoc = e.latlng;
            nextLoc = 1
            startMarker.setLatLng(e.latlng);
            startMarker.setOpacity(1);
            endMarker.setOpacity(0);
        } else if (nextLoc == 1 && !isProcessing) {
            isProcessing = true;
            endLoc = e.latlng;
            nextLoc = 0;
            endMarker.setLatLng(e.latlng);
            endMarker.setOpacity(1);
            document.getElementById("importing").innerHTML = "Calculating route, please wait"
            //TODO: route request
            $.post("MapServlet", String(startLoc) + String(endLoc), function (responseString) {
                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON
                rawData = responseString.split(" ");
                // polyline
                coords = [];
                //console.log(rawData);
                coords.push(startLoc);

                for (i = 0; i < (rawData.length / 2) - 1; i++) {
                    coords.push([rawData[2 * i], rawData[2 * i + 1]]);
                }
                coords.push(endLoc);
                //console.log(coords);
                var polyline = L.polyline(coords, {color: 'blue'}).addTo(mymap);
                isProcessing = false;
                document.getElementById("importing").innerHTML = "Done"
            });

        }
        console.log(startLoc, endLoc);
    }

    mymap.on('click', onMapClick);


    //vv auch in success methode
    //var polyline = L.polyline(coords, {color: 'blue'}).addTo(mymap);
    //mymap.fitBounds(polyline.getBounds());
    //^^
</script>

</html>
