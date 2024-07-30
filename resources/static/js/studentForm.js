$(document).ready(function () { // Hides the pickup/dropoff day selectors since they're not implemented yet
    $(".pickups, .dropoffs").hide();
});

$('#allPickup, #allDropoff').click(function(event) {   
    if(this.checked) {
        // Iterate each checkbox
        $(this).closest('.form-group').find(':checkbox').each(function() {
            this.checked = true;                        
        });
        
        //$('input:checkbox').not(this).prop('checked', this.checked);
    } else {
        $(this).closest('.form-group').find(':checkbox').each(function() {
            this.checked = false;                       
        });
    }
});


addressCounter = 1;
		
$('#plusIcon').click(function() {			
	if(addressCounter == 1) {
		$('#alt1').css("display","block");
		$('#minusIcon').css("display","inline-block");
		addressCounter++;
	}
	else if(addressCounter == 2) {
		$('#alt2').css("display","block");
		addressCounter++;
	}
	else if(addressCounter == 3) {
		$('#alt3').css("display","block");
		$('#plusIcon').css("display","none");
		addressCounter++;
	}
});

$('#minusIcon').click(function() {			
	if(addressCounter == 2) {
		$('#alt1').css("display","none");
		$('#minusIcon').css("display","none");
		addressCounter--;
	}
	else if(addressCounter == 3) {
		$('#alt2').css("display","none");
		addressCounter--;
	}
	else if(addressCounter == 4) {
		$('#alt3').css("display","none");
		$('#plusIcon').css("display","inline-block");
		addressCounter--;
	}
});