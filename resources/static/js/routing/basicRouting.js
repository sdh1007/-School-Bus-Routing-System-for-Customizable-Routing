
// Save's the current route when navigating off page using student's pickup/dropoff 'add' link
$('#pickupListbox, #dropoffListbox').on('click', 'a', function(e) { // Prevent any js activity from happening
   	e.preventDefault();
    
    link = $(this).attr("id");
    
    // If route exists, add current
    if(typeof pulledRoute !== 'undefined') {
		console.log(link)
		$('#routeForm').attr('action', link + "/" + pulledRoute.id); // Add route id if on the edit page
		console.log($('#routeForm').attr('action'));
	} else {
		$('#routeForm').attr('action', link);
	}
	
	if($("#routeName").val()) {
		//$("input[name='studentPickups'], input[name='studentDropoffs']").prop('checked', true); // Need to make all checked so Thymeleaf will grab their value
		
		$( "#routeForm" ).submit();
	} else {
		alert("Route's name is not entered.")
	}
});

$("form").submit(function(e) {			
	$("input[name='studentPickups'], input[name='studentDropoffs']").prop('checked', true); // Need to make all checked so Thymeleaf will grab their values
});

// Update and clear group related data
$("#group").on('change', function() {
	// Reset data fields
	$("#unassignedStudents").children().not(':first').remove();
	$("#pickupListbox").children().not(':first').remove(); // Can't combine the repeat calls into one b/c the not doesn't work
	$("#dropoffListbox").children().not(':first').remove();
	$("#startingLocations").find('.school').remove();// Can't combine the repeat calls into one b/c the not doesn't work
	$("#endingLocations").find('.school').remove();
	$("#totalStudents").val(0);
	$("#totalStops").val(0);
	$("#totalTravelTime").val(0);
	$("#totalTravelDist").val(0);

	groupStudents = [];
	groupSchools = [];

	// Get selected group object
	selectedGroup = groups.find(item => item.groupId == this.value);
	
	// Generate the array of student objects and append schools to start/end locations	
	selectedGroup.selectSchools.forEach((currentSchool) => { // Loop through all schools within group
		// Generate starting/ending point selection using school addresses
		var option = '<option value="' + currentSchool.id + '" class="school"> ' + currentSchool.schoolName + ' ('+currentSchool.city+')' + '</option>';
		$("#startingLocations, #endingLocations").append(option);
		
		groupStudents = groupStudents.concat(currentSchool.student); // Concat all students of the selected school
		groupSchools = groupSchools.concat(currentSchool);
    });
		
	// Generate students checkboxes
	groupStudents.forEach((currentStudent) => { // Loop through all schools within group
		var studentInput = '<input type="checkbox" name="students" class="form-check-input me-1" value="'+currentStudent.studentId+'">';
		
		var pPointText;
		var pPointId;
		if(currentStudent.pickupDropoff != null) {
    		pPointText = currentStudent.pickupDropoff.pickupDropoffName;
    		pPointId = currentStudent.pickupDropoff.pickupDropoffId;
    	} else {
    		pPointText = '<a href="#" id="/createSpecificPickupDropoff/'+currentStudent.studentId+'/'+selectedGroup.groupId+'">Add</a>';
    		pPointId = "";
    	}
		
		var unassignedHtml = '<label class="list-group-item">' + 
				           		'<div class="form-row">' + 
				               		'<div class="form-group col-md-1">' +
				               			studentInput +
							  		'</div>' +
							  		'<div class="form-group col-md-4">' +
							  			currentStudent.firstName + " " + currentStudent.lastName + 
							  		'</div>' +
							  		'<div class="form-group col-md-3">' +
							  			currentStudent.school.schoolName +
							  		'</div>' +
							  		'<div class="form-group col-md-2">' +
							  			currentStudent.grade +
							  		'</div>' +
							  		'<div class="form-group col-md-2 pPoint'+pPointId+'">' +
							  			pPointText +
							  		'</div>' +
				                 '</div>' +
				           '</label>';
		
		$("#unassignedStudents").append(unassignedHtml);
    });
});


// Update vehicle related data
$("#vehicle").on('change', function() {
	$("#startingLocations").find('.depot').remove();
	$("#endingLocations").find('.depot').remove();
	
	var selectedVehicle = vehicles.find(item => item.id == this.value);
	studentLimit = selectedVehicle.seatNumber;
	
	// If depot then generate location as start/ending location option
	if(selectedVehicle.depot != null) {
		var city = selectedVehicle.depot.city;
		
		var option = '<option value="' + selectedVehicle.depot.depotId + '" class="depot"> ' + 
			selectedVehicle.depot.name + ' ('+city+')' + '</option>';
		$("#startingLocations, #endingLocations").append(option);
	}
});

// Add option to start/end at driver location
$("#driver").on('change', function() {
	$("#startingLocations").find('.driver').remove();
	$("#endingLocations").find('.driver').remove();
	
	var selectedDriver = drivers.find(item => item.id == this.value);
	var city = selectedDriver.city;
	
	var option = '<option value="' + selectedDriver.id + '" class="driver"> ' + selectedDriver.name + ' ('+city+')' + '</option>';
	$("#startingLocations, #endingLocations").append(option);
});

$("#startingLocations, #endingLocations").on('change', function() {
	var selectedLocation = $("option:selected", this);
	var address;
	var city;
	var state;
	var zipcode;
	var id;
		
	if(selectedLocation.hasClass("school")) {				
		var selectedSchool = groupSchools.find(item => item.id == selectedLocation.val());
			
		address = selectedSchool.address;
		city = selectedSchool.city;
		state = selectedSchool.state;
		zipcode= selectedSchool.zip;
	}
	else if(selectedLocation.hasClass("depot")) {				
		var selectedDepot = depots.find(item => item.depotId == selectedLocation.val());
						
		address = selectedDepot.address1;
		city = selectedDepot.city;
		state = selectedDepot.state;
		zipcode= selectedDepot.zipCode;
	}
	else if(selectedLocation.hasClass("driver")) {
		var selectedDriver = drivers.find(item => item.id == selectedLocation.val());
		
		address = selectedDriver.address1;
		city = selectedDriver.city;
		state = selectedDriver.state;
		zipcode = selectedDriver.zipCode;
		id = selectedDriver.id;
	}

	if($(this).is("#startingLocations")) {
		// TODO Add check if ending location is selected & if it is the same position to not set map for both markers
		$("#startingLocationType").val(selectedLocation.attr("class"));
		addStartEndMarker(address, city, state, zipcode, id, true);
	} 
	else if($(this).is("#endingLocations")) {
		// TODO Add check if starting location is selected & if it is the same position to not set map for both markers
		
		$("#endingLocationType").val(selectedLocation.attr("class"));
		addStartEndMarker(address, city, state, zipcode, id, false);
	}
});

// Assign the student's to the route
$("#assignArrow").click(function(){						
    $.each($("input[name='students']:checked"), function(){
    	$("#totalStudents").val(Number($("#totalStudents").val()) + 1);
    	
    	// Prevent more student's than the bus can allow
    	if(Number($("#totalStudents").val()) > studentLimit) {
			alert("Can't add more student's than the bus allows!")
			return false;
		}
    	
    	var selectedStudent = students.find(item => item.studentId == $(this).val());
    	var studentPickupInput = '<input class="form-check-input me-1" type="checkbox" name="studentPickups" value="' + $(this).val() + '">'
    	var studentDropoffInput = '<input class="form-check-input me-1" type="checkbox" name="studentDropoffs" value="' + $(this).val() + '">'

    	var pPointText;
    	var pPointId;
    	
    	if(selectedStudent.pickupDropoff != null) {
    		pPointText = selectedStudent.pickupDropoff.pickupDropoffName;
    		pPointId = selectedStudent.pickupDropoff.pickupDropoffId;
    		
    		// Check which day the pickupDropoff is and decide which value to use
    		if(selectedStudent.pickupDropoff.defaultPickupDropoff == true || selectedStudent.pickupDropoff.monPickupPointLocation != null) {
    			pPointLatitude = selectedStudent.pickupDropoff.monPickupPointLocation.latitude;
        		pPointLongitude = selectedStudent.pickupDropoff.monPickupPointLocation.longitude;
    		} else if(selectedStudent.pickupDropoff.tuePickupPointLocation != null) {
    			pPointLatitude = selectedStudent.pickupDropoff.tuePickupPointLocation.latitude;
        		pPointLongitude = selectedStudent.pickupDropoff.tuePickupPointLocation.longitude;
    		} else if(selectedStudent.pickupDropoff.wedPickupPointLocation != null) {
    			pPointLatitude = selectedStudent.pickupDropoff.wedPickupPointLocation.latitude;
        		pPointLongitude = selectedStudent.pickupDropoff.wedPickupPointLocation.longitude;
    		} else if(selectedStudent.pickupDropoff.thurPickupPointLocation != null) {
    			pPointLatitude = selectedStudent.pickupDropoff.thurPickupPointLocation.latitude;
        		pPointLongitude = selectedStudent.pickupDropoff.thurPickupPointLocation.longitude;
    		} else if(selectedStudent.pickupDropoff.friPickupPointLocation != null) {
    			pPointLatitude = selectedStudent.pickupDropoff.friPickupPointLocation.latitude;
        		pPointLongitude = selectedStudent.pickupDropoff.friPickupPointLocation.longitude;
    		}
    		
    		// Check if the point was already added to the route
    		if(!gmarkers.find(item => item.id == pPointId)) {
				$("#totalStops").val(Number($("#totalStops").val()) + 1);
				
    			addPickupDropoffMarker(pPointLatitude, pPointLongitude, pPointId, pPointText);
    		} else {
    			changeMarkerInstance(pPointId, "increase");
    		}
    	} else {
    		pPointText = '<a href="#" id="/createSpecificPickupDropoff/'+selectedStudent.studentId+'/'+selectedGroup.groupId+'">Add</a>';
    		pPointId = "";
    	}
    	
		var pickupHtml = '<label class="list-group-item">' + 
			           		'<div class="form-row">' + 
			               		'<div class="form-group col-md-1">' +
			               			studentPickupInput +
						  		'</div>' +
						  		'<div class="form-group col-md-6">' +
						  			selectedStudent.firstName + " " + selectedStudent.lastName + 
						  		'</div>' +
						  		'<div class="form-group col-md-3 pPoint">' +
						  			pPointText +
						  		'</div>' +
						  		'<div class="form-group col-md-2">' +
						  			'08:00' +
						  		'</div>' +
			                 '</div>' +
			           '</label>';
		var dropoffHtml = '<label class="list-group-item">' + 
			           		'<div class="form-row">' + 
			               		'<div class="form-group col-md-1">' +
			               			studentDropoffInput +
						  		'</div>' +
						  		'<div class="form-group col-md-6">' +
						  			selectedStudent.firstName + " " + selectedStudent.lastName + 
						  		'</div>' +
						  		'<div class="form-group col-md-3 pPoint">' +
						  			pPointText +
						  		'</div>' +
						  		'<div class="form-group col-md-2">' +
						  			'3:00' +
						  		'</div>' +
			                 '</div>' +
		           		'</label>';
		
		if(pPointId != "") { // Might be faster without this? Since the next if will fail regardless if pPointId is null
			if($("."+pPointId+".studentGroup").length) { // Check if the pickup/dropoff div exists already
				$("#pickupListbox " + "."+pPointId+".studentGroup").append(pickupHtml); // Add to already assigned pickup
				$("#dropoffListbox " + "."+pPointId+".studentGroup").append(dropoffHtml); // Add to already assigned dropoff
			} else {
				var pickupDiv = $('<div class="'+pPointId+' studentGroup"></div>');
				pickupDiv.append(pickupHtml);
				var dropoffDiv = $('<div class="'+pPointId+' studentGroup"></div>');
				dropoffDiv.append(dropoffHtml);
				
				$(pickupDiv).clone().appendTo("#pickupListbox"); // Add div with student to assigned pickup
				$(dropoffDiv).insertAfter("#dropoffListbox .listHeadings"); // Add div with student to assigned dropoff
				
				// Check if any other student's have this point & indicate to the user that they do
				$('#unassignedStudents .pPoint'+pPointId).parents('.list-group-item').css("background-color","rgba(0,112,85,0.2)");
			}
		} else {
			if($(".noPoint").length) { // Check if the pickup/dropoff div exists already
				$("#pickupListbox .noPoint").append(pickupHtml); // Add to already assigned pickup
				$("#dropoffListbox .noPoint").append(dropoffHtml); // Add to already assigned dropoff
			} else {
				var pickupDiv = $('<div class="noPoint studentGroup"></div>');
				pickupDiv.append(pickupHtml);
				var dropoffDiv = $('<div class="noPoint studentGroup"></div>');
				dropoffDiv.append(dropoffHtml);
				
				$(pickupDiv).clone().appendTo("#pickupListbox"); // Add div with student to assigned pickup
				$(dropoffDiv).insertAfter("#dropoffListbox .listHeadings"); // Add div with student to assigned dropoff
			}
		}
		
		$(this).parents(".list-group-item").remove();
    });
});

// Unassign the checked students in either assigned Pickup or assigned Dropoff
$("#deAssignArrow").mouseup(function(){
	$.each($("input[name='studentPickups']:checked, input[name='studentDropoffs']:checked"), function(){
		$("#totalStudents").val(Number($("#totalStudents").val()) - 1);
		
		var selectedStudent = students.find(item => item.studentId == $(this).val());
		
		// Create label and add student back to the unassigned student's list
		var studentInput = '<input type="checkbox" name="students" class="form-check-input me-1" value="'+selectedStudent.studentId+'">';
		
		var pPointText;
		var pPointId;
		if(selectedStudent.pickupDropoff != null) {
    		pPointText = selectedStudent.pickupDropoff.pickupDropoffName;
    		pPointId = selectedStudent.pickupDropoff.pickupDropoffId;
    	} else {
    		pPointText = '<a href="#" id="/createSpecificPickupDropoff/'+selectedStudent.studentId+'/'+selectedGroup.groupId+'">Add</a>';
    		pPointId = "";
    	}
		
		var unassignedHtml = '<label class="list-group-item">' + 
			           		'<div class="form-row">' + 
			               		'<div class="form-group col-md-1">' +
			               			studentInput +
						  		'</div>' +
						  		'<div class="form-group col-md-4">' +
						  			selectedStudent.firstName + " " + selectedStudent.lastName + 
						  		'</div>' +
						  		'<div class="form-group col-md-3">' +
						  			selectedStudent.school.schoolName +
						  		'</div>' +
						  		'<div class="form-group col-md-2">' +
						  			selectedStudent.grade +
						  		'</div>' +
						  		'<div class="form-group col-md-2 pPoint'+pPointId+'">' +
						  			pPointText +
						  		'</div>' +
			                 '</div>' +
			           '</label>';
			           
		$("#unassignedStudents").append(unassignedHtml);
		
		// Determine if the student group or just one element needs removed
		var parentRemove;
		if($(this).parents(".studentGroup").children().length > 1) {
			parentRemove = "list-group-item";
		} else {
			parentRemove = "studentGroup";
		}
		
		// Checks which listbox the checkbox is in to know which other list to remove the checkbox from
		if($(this).parents(".list-group").prop('id') == "pickupListbox") {
			$('#dropoffListbox :input[value="'+selectedStudent.studentId+'"]').parents('.'+parentRemove).remove(); // Find the checkbox associated with the currently checked checkbox & remove the label parent
		} else {
			$('#pickupListbox :input[value="'+selectedStudent.studentId+'"]').parents('.'+parentRemove).remove();
		}
		
		$(this).parents('.'+parentRemove).remove();
		if(pPointId != "") {
			changeMarkerInstance(pPointId, "decrease"); // Either decrease the instance or remove the waypoint & marker 
		}			
    });
});

// Move the selected group of student's up or down in the assigned pickup list or assigned dropoff list
$("#pickupDown,  #dropoffDown, #pickupUp, #dropoffUp").mouseup(function(){
	var prevStudGroupId = null;
	var inputSelect = "";
	var arrowId = $(this).attr("id");
		
	if(arrowId == "pickupDown" || arrowId == "pickupUp") {
		inputSelect = "input[name='studentPickups']:checked";
	} else {
		inputSelect = "input[name='studentDropoffs']:checked";
	}
		
	$.each($(inputSelect), function(){
		var studentGroup = $(this).parents(".studentGroup");
		var studentGroupId = $(studentGroup).attr('class').split(' ')[0]; // Gets first class, not .studentGroup which is the stop id
		
		// Prevents case where user selects mult studs from same group and causes it to skip over many groups
		if(prevStudGroupId == studentGroupId) {
			return true; // Skip iteration if student from same group was selected
		} else {
			prevStudGroupId = studentGroupId; // Set prevGroup
			
			// Check if an up or down arrow was selected
			if(arrowId == "pickupUp" || arrowId == "dropoffUp") {
				// Check if the group is the first in the list
				if($(studentGroup).index() == 1) {
					alert("Cannot move up, student's stop is already first!");
				} else {
					$(studentGroup).insertBefore($(studentGroup).prev());
					
					if(studentGroupId != "noPoint") {
						if($(studentGroup).parent().prop("id") == "pickupListbox") {
							moveStop(studentGroupId, "pickup", "up");
						} else {
							moveStop(studentGroupId, "dropoff", "up");
						}
					}
				}
			} else {
				// Checks if group is last in the list
				if($(studentGroup).index() == ($(studentGroup).parent().children().length - 1)) {
					alert("Cannot move down, student's stop is already last!");
				} else {
					$(studentGroup).insertAfter($(studentGroup).next());
					
					if(studentGroupId != "noPoint") {							
						if($(studentGroup).parent().prop("id") == "pickupListbox") {
							moveStop(studentGroupId, "pickup", "down");
						} else {
							moveStop(studentGroupId, "dropoff", "down");
						}
					}
				}
			}
		}
	});
});

// Checks if the associated pickup/dropoff 
$("#pickupListbox").on('change', 'input:checkbox', function() {
	selectedStudentId = $(this).val();
	
	if($('#dropoffListbox :input[value="'+selectedStudentId+'"]').is(":checked")) {
		$(this).prop('checked', !$(this).prop('checked'));
		alert("Can't select the same student twice!");
	}; 
});

// Checks if the associated pickup/dropoff 
$("#dropoffListbox").on('change', 'input:checkbox', function() {			
	selectedStudentId = $(this).val();

	if($('#pickupListbox :input[value="'+selectedStudentId+'"]').is(":checked")) {
		$(this).prop('checked', !$(this).prop('checked'));
		alert("Can't select the same student twice!");
	};
});

$("#calcPickupRoute").click(function() {	
	calcRoute("pickup");
});

$("#calcDropoffRoute").click(function() {	
	calcRoute("dropoff");
});

// Calculates the route
function calcRoute(routeType) {
	if(waypts.length == 0) {
		window.alert("Please assign students with a pickup/dropoff to the route!");
		return;
	} else if(startMarker == null || endMarker == null) {
		window.alert("Please assign a starting & ending point to the route!");
		return;
	}
	
	clearAllRoutes();
	
	if (routeType == "pickup") {
		var directionsService = new google.maps.DirectionsService();
		var directionsRenderer = new google.maps.DirectionsRenderer({ // Set route options 
			map,
			suppressMarkers: true, 
			polylineOptions: {
			    strokeColor: '#007055',
			    strokeOpacity: 0.6,
			    strokeWeight: 5,
			}
		});
		
		var request = {
		    origin: startMarker.position,
		    destination: endMarker.position,
		    waypoints: waypts,
	      	travelMode: google.maps.TravelMode.DRIVING,
	      	unitSystem: google.maps.UnitSystem.IMPERIAL,
	  	};
	  	
	 	directionsService.route(request, function(result, status) {
	    	if (status == 'OK') {
	      		directionsRenderer.setDirections(result);
	      		
	      		$("#pickupTotalTravelTime").val(getTotalDuration(result.routes[0]));
	      		$("#pickupTotalDistance").val(getTotalDistance(result.routes[0]));
	      		$("#pickupTotalTravelTimeWithoutRiders").val(result.routes[0].legs[0].duration.text);
	      		
	      		routes.push(directionsRenderer);
	  		}
	    	else {
	    		window.alert("Error finding a route, please try again. Error: " + status);
	    	}
	  	});
	} else {
		var directionsService = new google.maps.DirectionsService();
		var directionsRenderer = new google.maps.DirectionsRenderer({ // Set route options 
			map,
			suppressMarkers: true, 
			polylineOptions: {
			    strokeColor: '#6f0000',
			    strokeOpacity: 0.6,
			    strokeWeight: 5,
			}
		});
		
		var request = {
		    origin: endMarker.position,
		    destination: startMarker.position,
		    waypoints: dropoffWaypts,
	      	travelMode: google.maps.TravelMode.DRIVING,
	      	unitSystem: google.maps.UnitSystem.IMPERIAL,
	  	};
	  	
	 	directionsService.route(request, function(result, status) {
	    	if (status == 'OK') {
	      		directionsRenderer.setDirections(result);
	      		
	      		var lastLeg = result.routes[0].legs.length - 1;
	      		$("#dropoffTotalTravelTime").val(getTotalDuration(result.routes[0]));
	      		$("#dropoffTotalDistance").val(getTotalDistance(result.routes[0]));
	      		$("#dropoffTotalTravelTimeWithoutRiders").val(result.routes[0].legs[lastLeg].duration.text);
	      		
	      		routes.push(directionsRenderer);
	  		}
	    	else {
	    		window.alert("Error finding a route, please try again. Error: " + status);
	    	}
	  	});
	}
	
	function getTotalDistance(route) {
	    var totalDistance = 0;
	    var legs = route.legs;
	    for(var i=0; i<legs.length; ++i) {
	      totalDistance += legs[i].distance.value;
	    }
	    
	    totalDistance = totalDistance * 0.000621371192; // Convert from meters to miles
	    
	    return totalDistance.toFixed(2) + " mi"; // Limit to 2 decimals
    }
	
	function getTotalDuration(route) {
	    var totalDuration = 0;
	    var legs = route.legs;
	    for(var i=0; i<legs.length; ++i) {
	      totalDuration += legs[i].duration.value; 
	    }

	    // Converts the duration to hours and minutes
	    var h = Math.floor(totalDuration / 3600);
	    var m = Math.floor(totalDuration % 3600 / 60);

	    var hDisplay = h > 0 ? h + (h == 1 ? " hour, " : " hrs, ") : "";
	    var mDisplay = m > 0 ? m + (m == 1 ? " minute, " : " mins ") : "";
	    return hDisplay + mDisplay; 
    }
}

// Adds a marker with the given address
function addStartEndMarker(addr, cty, sta, zp, id, startEnd) {
	var location = addr+" "+cty+" "+sta+" "+zp;
	
	geocoder.geocode({address: location}, function(results){
		var lat = results[0].geometry.location.lat();
		var lng = results[0].geometry.location.lng();
		var position = results[0].geometry.location;
		
		if(startEnd == true) {
			if(startMarker != null) {
				startMarker.position = position;
			} else {
				const pinViewVisuals = new google.maps.marker.PinView({
				    background: "#007055",
				    glyphColor: "#ffffff",
				    borderColor: "#143642",
				});
				
				startMarker = new google.maps.marker.AdvancedMarkerView({
					map,
					position: position,
					content: pinViewVisuals.element,
					title: "Starting Location",
					id: id,
				});
			}
			
		} else {
			if(endMarker != null) {
				endMarker.position = position;
			} else {
				const pinViewVisuals = new google.maps.marker.PinView({
				    background: "#6f0000",
				    glyphColor: "#ffffff",
				    borderColor: "#143642",
				});
				
				endMarker = new google.maps.marker.AdvancedMarkerView({
					map,
					position: position,
					content: pinViewVisuals.element,
					title: "Ending Location",
					id: id,
				});
			}
			
		}
		map.setCenter(new google.maps.LatLng(lat, lng));
	});
};

// Adds a marker for the pickup/dropoff point
function addPickupDropoffMarker(lat, lng, id, name) {			
	const marker = new google.maps.Marker({  
		id: id,
		position: { lat: lat, lng: lng},
		title: "Name: " + name,
		map: map,
		instances: 1,
	});
	
	gmarkers.push(marker);
	dropoffGmarkers.unshift(marker); // Add reversed order dropoff marker
	
	addWaypoint(lat, lng); // Adds both types of waypoints
	map.setCenter(new google.maps.LatLng(lat, lng));
};

// Adds a pickup/dropoff
function addWaypoint(lat, lng) {
	waypt = {
		location: { lat: lat, lng: lng },
        stopover: true,
	}
				
	waypts.push(waypt);
	dropoffWaypts.unshift(waypt); // Add reversed order dropoff waypoint
}

// increases or decreases an instance of a pickup/dropoff marker
function changeMarkerInstance(id, option) {			
	var selectedMarker = gmarkers.find(item => item.id == id);
	
	if(selectedMarker != null) {
		if(option == "increase") {
			selectedMarker.instances++;
		} else if(option == "decrease") {
			if(selectedMarker.instances == 1) {
				$("#totalStops").val(Number($("#totalStops").val()) - 1);
				
				// Remove the indication that the stop is on the route for other students
				$('#unassignedStudents .pPoint'+id).parents('.list-group-item').css("background-color","");
				
				deleteWaypt(id); // Does it for both
				deleteMarker(id); // Does it for both
			} else {
				// Check if any other student's have this point & indicate to the user that they do
				$('#unassignedStudents .pPoint'+id).parents('.list-group-item').css("background-color","rgba(0,112,85,0.2)");
				
				selectedMarker.instances--;
			}
		}
	}
};

// Moves a stop for either the pickup or dropoff route up or down
function moveStop(id, listType, moveType) {
	var toIndex;
	
	if(listType == "pickup") {
		// Find needed data
    	const marker = gmarkers.find(item => item.id == id);
		const markerIndex = gmarkers.indexOf(marker);
		const waypt = waypts[markerIndex];
		
		// Determine moving up or down & if it is the first or last index
		if(moveType == "up") {
			if(markerIndex == 0) {
				return;
			}
			
			toIndex = markerIndex - 1;
		} else if(moveType == "down"){
			if(markerIndex == (waypts.length - 1)) {
				return;
			}
			toIndex = markerIndex + 1;
		} else {
			return;
		}
		
		// Delete marker index & waypoint index element
		gmarkers = gmarkers.filter(item => item != marker);
		waypts.splice(markerIndex, 1);
		
		// Add element to new index;
		gmarkers.splice(toIndex, 0, marker);
		waypts.splice(toIndex, 0, waypt);
	} else {	    		
		// Find needed data
    	const marker = dropoffGmarkers.find(item => item.id == id);
		const markerIndex = dropoffGmarkers.indexOf(marker);
		const waypt = dropoffWaypts[markerIndex];
		
		// Determine moving up or down & if it is the first or last index
		if(moveType == "up") {
			if(markerIndex == 0) {
				return; // Already at top
			}
			
			toIndex = markerIndex - 1;
		} else if(moveType == "down"){
			if(markerIndex == (dropoffWaypts.length - 1)) {
				return; // Already at bottom
			}
			toIndex = markerIndex + 1;
		} else {
			return; // Not up or down
		}
		
		// Delete marker index & waypoint index element
		dropoffGmarkers = dropoffGmarkers.filter(item => item != marker);
		dropoffWaypts.splice(markerIndex, 1);
		
		// Add element to new index;
		dropoffGmarkers.splice(toIndex, 0, marker);
		dropoffWaypts.splice(toIndex, 0, waypt);
	}
}

var deleteMarker = function(id) {			
	var marker = gmarkers.find(item => item.id == id);// find the marker by given id		
    marker.setMap(null); // Remove marker from map
    
	gmarkers = gmarkers.filter(marker => marker.id != id); // Remove marker from list of markers
	
	dropoffGmarkers = dropoffGmarkers.filter(marker => marker.id != id); // Remove dropoff marker from list of markers
}

var deleteWaypt = function(id) {		
	var marker = gmarkers.find(item => item.id == id);// find the marker associated with the waypnt	
	var markerIndex = gmarkers.indexOf(marker); // Get index of the marker to find the waypnt
	
	waypts.splice(markerIndex, 1); // Remove the waypnt using marker
	
	// Remove the dropoff waypoint the same way
	var marker = dropoffGmarkers.find(item => item.id == id); // find the marker associated with the waypnt	
	var markerIndex = dropoffGmarkers.indexOf(marker); // Get index of the marker to find the waypnt
	
	dropoffWaypts.splice(markerIndex, 1); // Remove the waypnt using marker
}

var deleteRoute = function(id) {			
	var route = routes.find(route => route.id == id); // find the route by given id
	route.setMap(null); // Remove route from map
	
	routes = routes.filter(route => route.id != id); // Remove route from list of routes
}

// Deletes all markers
function clearAllMarkers() {						
    for (var i = 0; i < gmarkers.length; i++) {	        	
    	gmarkers[i].setMap(null);
    }
    
    gmarkers = [];
}

// Deletes all waypoints
function clearAllWaypts() {							
	waypts = [];
}

// Deletes all routes
function clearAllRoutes() {
	for (var i = 0; i < routes.length; i++) {
		routes[i].setMap(null);
	}
	
	routes = [];
}

// Clears all map indicators
function clearMap() {
	clearAllMarkers();
	clearAllRoutes();
	clearAllWaypts();
}