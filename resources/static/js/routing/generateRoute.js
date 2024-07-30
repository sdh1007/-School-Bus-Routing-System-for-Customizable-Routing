function prepareForm() {
	var group = pulledRoute.group;
	var pickupStudents = pulledRoute.pickupStudents;
	var dropoffStudents = pulledRoute.dropoffStudents;
  	
  	// Get selected group object
	selectedGroup = group;
  	
	if(pulledRoute.vehicle) {
		studentLimit = pulledRoute.vehicle.seatNumber;
	}
	
	// Generate the schools starting and ending locations
	group.selectSchools.forEach(function(currentSchool) { // Loop through all schools within group				
		if(currentSchool.student.length > 0) { // Check if school has students otherwise it doesn't matter
			groupStudents = groupStudents.concat(currentSchool.student); // Concat all students of the selected school
			groupSchools = groupSchools.concat(currentSchool);
		}
	
		// Generate starting/ending point selection using school addresses
		var option = $('<option value="' + currentSchool.id + '" class="school"> ' + currentSchool.schoolName + ' ('+currentSchool.city+')' + '</option>');
		
		// Check if the option should be selected or not  -- NOT A GREAT SOLUTION & SHOULD BE CHANGED
		if(pulledRoute.startingLocation && pulledRoute.endingLocation) {
			if(currentSchool.address == pulledRoute.startingLocation.address1 && currentSchool.address == pulledRoute.endingLocation.address1) {
				$(option).attr("selected", "selected");
				
				$("#startingLocations, #endingLocations").append(option);
				
				$("#startingLocationType").val("school");
				$("#endingLocationType").val("school");
				addStartEndMarker(currentSchool.address, currentSchool.city, currentSchool.state, currentSchool.zip, currentSchool.id, true);
				addStartEndMarker(currentSchool.address, currentSchool.city, currentSchool.state, currentSchool.zip, currentSchool.id, false);
			} else if(currentSchool.address == pulledRoute.startingLocation.address1) {
				$(option).clone().appendTo("#endingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#startingLocations").append(option);
				
				$("#startingLocationType").val("school");
				addStartEndMarker(currentSchool.address, currentSchool.city, currentSchool.state, currentSchool.zip, currentSchool.id, true);
			} else if(currentSchool.address == pulledRoute.endingLocation.address1) {
				$(option).clone().appendTo("#startingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#endingLocations").append(option);
				$("#endingLocationType").val("school");
				addStartEndMarker(currentSchool.address, currentSchool.city, currentSchool.state, currentSchool.zip, currentSchool.id, false);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else if(pulledRoute.startingLocation) {
			if(currentSchool.address == pulledRoute.startingLocation.address1) {
				$(option).clone().appendTo("#endingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#startingLocations").append(option);
				
				$("#startingLocationType").val("school");
				addStartEndMarker(currentSchool.address, currentSchool.city, currentSchool.state, currentSchool.zip, currentSchool.id, true);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else if(pulledRoute.endingLocation) {
			if(currentSchool.address == pulledRoute.endingLocation.address1) {
				$(option).clone().appendTo("#startingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#endingLocations").append(option);
				$("#endingLocationType").val("school");
				addStartEndMarker(currentSchool.address, currentSchool.city, currentSchool.state, currentSchool.zip, currentSchool.id, false);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else {
			$("#startingLocations, #endingLocations").append(option);
		}
    });
	
	// If depot then generate location as start/ending location option -- NOT A GREAT SOLUTION & SHOULD BE CHANGED
	if(pulledRoute.vehicle && pulledRoute.vehicle.depot) {
		var depot = pulledRoute.vehicle.depot;
		var option = $('<option value="' + depot.depotId + '" class="depot"> ' + depot.name + ' ('+depot.city+')' + '</option>');
		
		if(pulledRoute.startingLocation && pulledRoute.endingLocation) {
			if(depot.address1 == pulledRoute.startingLocation.address1 && depot.address1 == pulledRoute.endingLocation.address1) {
				$(option).attr("selected", "selected");
				
				$("#startingLocations, #endingLocations").append(option);
				
				$("#startingLocationType").val("depot");
				$("#endingLocationType").val("depot");
				addStartEndMarker(depot.address1, depot.city, depot.state, depot.zipCode, depot.depotId, true);
				addStartEndMarker(depot.address1, depot.city, depot.state, depot.zipCode, depot.depotId, false);
			} else if(depot.address1 == pulledRoute.startingLocation.address1) {
				$(option).clone().appendTo("#endingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#startingLocations").append(option);
				
				$("#startingLocationType").val("depot");
				addStartEndMarker(depot.address1, depot.city, depot.state, depot.zipCode, depot.depotId, true);
			} else if(depot.address1 == pulledRoute.endingLocation.address1) {
				$(option).clone().appendTo("#startingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#endingLocations").append(option);
				
				$("#endingLocationType").val("depot");
				addStartEndMarker(depot.address1, depot.city, depot.state, depot.zipCode, depot.depotId, false);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else if(pulledRoute.startingLocation) {
			if(depot.address1 == pulledRoute.startingLocation.address1) {
				$(option).clone().appendTo("#endingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#startingLocations").append(option);
				
				$("#startingLocationType").val("depot");
				addStartEndMarker(depot.address1, depot.city, depot.state, depot.zipCode, depot.depotId, true);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else if(pulledRoute.endingLocation) {
			if(depot.address1 == pulledRoute.endingLocation.address1) {
				$(option).clone().appendTo("#startingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#endingLocations").append(option);
				
				$("#endingLocationType").val("depot");
				addStartEndMarker(depot.address1, depot.city, depot.state, depot.zipCode, depot.depotId, false);
			}
		} else {
			$("#startingLocations, #endingLocations").append(option);
		}
	}
	
	// If driver then generate location as start/ending location option  -- NOT A GREAT SOLUTION & SHOULD BE CHANGED
	if(pulledRoute.driver) {
		var driver = pulledRoute.driver;
		var option = $('<option value="' + driver.id + '" class="driver"> ' + driver.name + ' ('+driver.city+')' + '</option>');
		
		if(pulledRoute.startingLocation && pulledRoute.endingLocation) {
			if(driver.address1 == pulledRoute.startingLocation.address1 && driver.address1 == pulledRoute.endingLocation.address1) {
				$(option).attr("selected", "selected");
				
				$("#startingLocations, #endingLocations").append(option);
				
				$("#startingLocationType").val("driver");
				$("#endingLocationType").val("driver");
				addStartEndMarker(driver.address1, driver.city, driver.state, driver.zipCode, driver.id, true);
				addStartEndMarker(driver.address1, driver.city, driver.state, driver.zipCode, driver.id, false);
			} else if(driver.address1 == pulledRoute.startingLocation.address1) {
				$(option).clone().appendTo("#endingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#startingLocations").append(option);
				
				$("#startingLocationType").val("driver");
				addStartEndMarker(driver.address1, driver.city, driver.state, driver.zipCode, driver.id, true);
			} else if(driver.address1 == pulledRoute.endingLocation.address1) {
				$(option).clone().appendTo("#startingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#endingLocations").append(option);
				
				$("#endingLocationType").val("driver");
				addStartEndMarker(driver.address1, driver.city, driver.state, driver.zipCode, driver.id, false);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else if(pulledRoute.startingLocation) {
			if(driver.address1 == pulledRoute.startingLocation.address1) {
				$(option).clone().appendTo("#endingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#startingLocations").append(option);
				
				$("#startingLocationType").val("driver");
				addStartEndMarker(driver.address1, driver.city, driver.state, driver.zipCode, driver.id, true);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else if(pulledRoute.endingLocation) {
			if(driver.address1 == pulledRoute.endingLocation.address1) {
				$(option).clone().appendTo("#startingLocations"); // Add non selected option
				
				$(option).attr("selected", "selected");
				
				$("#endingLocations").append(option);
				
				$("#endingLocationType").val("driver");
				addStartEndMarker(driver.address1, driver.city, driver.state, driver.zipCode, driver.id, false);
			} else {
				$("#startingLocations, #endingLocations").append(option);
			}
		} else {
			$("#startingLocations, #endingLocations").append(option);
		}
	}
	
	// Generate unassigned students except students that are already assigned to the route
	$("#unassignedStudents .default-item").remove(); // Remove the default value since students in a group are required for a route to be saved
	
	// Add student's to the unassigned list if they are not found in the pickupStudents list	
	groupStudents.forEach((currentStudent) => {
		if(!pickupStudents.find(item => item.studentId == currentStudent.studentId)) {
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
		}
    });
	
	// Generate pickup students
	pickupStudents.forEach((selectedStudent) => {
		var studentPickupInput = '<input class="form-check-input me-1" type="checkbox" name="studentPickups" value="' + selectedStudent.studentId + '">'
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
    		
    		// Check if the point was already added to the route - doesn't happen when generating dropoffStudents to prevent wrongly increasing instance
			if(!gmarkers.find(item => item.id == pPointId)) {						
				addPickupDropoffMarker(pPointLatitude, pPointLongitude, pPointId, pPointText);
			} else {
				changeMarkerInstance(pPointId, "increase");
			}
						
			// Check if any other student's have this point & indicate to the user that they do
			$('#unassignedStudents .pPoint'+pPointId).parents('.list-group-item').css("background-color","rgba(0,112,85,0.2)");
    	} else {
    		pPointText = '<a href="#" id="/createSpecificPickupDropoff/'+selectedStudent.studentId+'/'+selectedGroup.groupId+'">Add</a>';
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
			           
       if(pPointId != null) { // Might be faster without this? Since the next if will fail regardless if pPointId is null
			if($("."+pPointId+"").length) { // Check if the pickup/dropoff div exists already
				$("#pickupListbox " + "."+pPointId).append(pickupHtml); // Add to already assigned pickup
			} else {
				var pickupDiv = $('<div class="'+pPointId+' studentGroup"></div>');
				pickupDiv.append(pickupHtml);
				
				$(pickupDiv).clone().appendTo("#pickupListbox"); // Add div with student to assigned pickup
			}
		} else {
			if($(".noPoint").length) { // Check if the pickup/dropoff div exists already
				$("#pickupListbox .noPoint").append(pickupHtml); // Add to already assigned pickup
			} else {
				var pickupDiv = $('<div class="noPoint studentGroup"></div>');
				pickupDiv.append(pickupHtml);

				$(pickupDiv).clone().appendTo("#pickupListbox"); // Add div with student to assigned pickup
			}
		}
    });
	
	
	// Generate dropoff students
	dropoffStudents.forEach((selectedStudent) => {
    	var studentDropoffInput = '<input class="form-check-input me-1" type="checkbox" name="studentDropoffs" value="' + selectedStudent.studentId + '">'

    	var pPointText;
    	var pPointId;
    	
    	if(selectedStudent.pickupDropoff != null) {
    		pPointText = selectedStudent.pickupDropoff.pickupDropoffName;
    		pPointId = selectedStudent.pickupDropoff.pickupDropoffId;
    	} else {
    		pPointText = '<a href="#" id="/createSpecificPickupDropoff/'+selectedStudent.studentId+'/'+selectedGroup.groupId+'">Add</a>';
    	}
    	
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
		
   		if(pPointId != null) { // Might be faster without this? Since the next if will fail regardless if pPointId is null
   			if($("#dropoffListbox ."+pPointId+"").length) { // Check if the pickup/dropoff div exists already
				$("#dropoffListbox " + "."+pPointId).append(dropoffHtml); // Add to already assigned dropoff 
			} else {
				var dropoffDiv = $('<div class="'+pPointId+' studentGroup"></div>');
				dropoffDiv.append(dropoffHtml);

				$(dropoffDiv).appendTo("#dropoffListbox"); // Add div with student to assigned dropoff
			}
		} else {
			if($("#dropoffListbox .noPoint").length) { // Check if the pickup/dropoff div exists already
				$("#dropoffListbox .noPoint").append(dropoffHtml); // Add to already assigned dropoff
			} else {
				var dropoffDiv = $('<div class="noPoint studentGroup"></div>');
				dropoffDiv.append(dropoffHtml);
				
				$(dropoffDiv).appendTo("#dropoffListbox"); // Add div with student to assigned dropoff
			}
		}
	});
};