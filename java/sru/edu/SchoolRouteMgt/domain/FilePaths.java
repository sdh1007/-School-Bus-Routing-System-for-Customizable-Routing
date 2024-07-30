package sru.edu.SchoolRouteMgt.domain;
/* 
 * Describes all of the file paths for all of the files used in the program
 * */
public class FilePaths {
	// controllers
	private String appController = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/controller/AppController.java";
	private String fileController = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/controller/FileController.java";
	private String pickupDropoffController = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/controller/PickupDropoffController.java";
	private String routingController = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/controller/RoutingController.java";
	private String schoolRouteMgtController = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/controller/SchoolRouteMgtController.java";
	// datawriter
	private String CreateFile = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/datawriter/CreateFile.java ";
	private String WriteToLog = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/datawriter/WriteToLog.java ";
	// domain
	private String CustomUserDetails = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/CustomUserDetails.java";
	private String DataLog = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/DataLog.java";
	private String Depot = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/Depot.java";
	private String District = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/District.java";
	private String Drivers = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/Drivers.java";
	private String FileInfo = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/FileInfo.java";
	private String LocationPoint = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/LocationPoint.java";
	private String PickupDropoff = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/PickupDropoff.java";
	private String Role = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/Role.java";
	private String RouteGroups = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/RouteGroups.java";
	private String Routing = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/Routing.java";
	private String Schools = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/Schools.java";
	private String Students = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/Students.java";
	private String User = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/User.java";
	private String Vehicle = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/Vehicle.java";
	// exception
	private String FileUploadExceptionAdvice = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/exception/FileUploadExceptionAdvice.java";
	// helper
	private String CsvDepotHelper = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/helper/CsvDepotHelper.java";
	private String CsvDriverHelper = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/helper/CsvDriverHelper.java";
	private String CsvHelper = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/helper/CsvHelper.java";
	private String CsvSchoolHelper = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/helper/CsvSchoolHelper.java";
	private String CsvStudentHelper = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/helper/CsvStudentHelper.java";
	private String CsvVehicleHelper = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/helper/CsvVehicleHelper.java";
	private String ImageHelper = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/helper/ImageHelper.java";
	// repository
	private String DataLogRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/DataLogRepository.java";
	private String DepotRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/DepotRepository.java";
	private String DistrictRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/DistrictRepository.java";
	private String DriverRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/DriverRepository.java";
	private String FileRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/FileRepository.java";
	private String LocationPointRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/LocationPointRepository.java";
	private String PickupDropoffRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/PickupDropoffRepository.java";
	private String RoleRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/RoleRepository.java";
	private String RouteGroupsRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/RouteGroupsRepository.java";
	private String RoutingRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/RoutingRepository.java";
	private String SchoolRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/SchoolRepository.java";
	private String StudentRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/StudentRepository.java";
	private String UserRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/repository/UserRepository.java";
	private String VehicleRepository = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/domain/VehicleRepository.java";
	// security
	private String WebSecurityConfig = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/security/WebSecurityConfig.java";
	// services
	private String CsvService = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/services/CsvService.java";
	private String CustomUserDetailsService = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/services/CustomUserDetailsService.java";
	private String DatabaseBackupService = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/services/DatabaseBackupService.java";
	private String FilesStorageService = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/services/FilesStorageService.java";
	private String FilesStorageServiceImpl = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/services/FilesStorageServiceImpl.java";
	private String UserService = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/services/UserService.java";
	// to database
	private String LoadDistrict = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/toDatabase/LoadDistrict.java";
	private String LoadRole = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/toDatabase/LoadRole.java";
	private String LoadUser = "/SchoolRouteMgt/src/main/java/sru/edu/SchoolRouteMgt/toDatabase/LoadUser.java";
	//     resources
	//   static
	// css
	private String styleCss = "/SchoolRouteMgt/src/main/resources/static/css/style.css";
	// imgs
	private String logoffButton = "/SchoolRouteMgt/src/main/resources/static/imgs/logoffButton.png";
	private String minusIcon = "/SchoolRouteMgt/src/main/resources/static/imgs/minusIcon.png";
	private String plusIcon = "/SchoolRouteMgt/src/main/resources/static/imgs/plusIcon.png";
	private String schoolIcon = "/SchoolRouteMgt/src/main/resources/static/imgs/schoolIcon.png";
	// js
	private String basicRoutingJs = "/SchoolRouteMgt/src/main/resources/static/js/routing/basicRouting.js";
	private String generateRouteJs = "/SchoolRouteMgt/src/main/resources/static/js/routing/generateRoute.js";
	private String dataTableScriptJs = "/SchoolRouteMgt/src/main/resources/static/js/dataTableScript.js";
	private String studentFormJs = "/SchoolRouteMgt/src/main/resources/static/js/studentForm.js";
	//   templates
	// fragments
	private String addEditPagesFrag = "/SchoolRouteMgt/src/main/resources/templates/fragments/addEditPages.html";
	private String headingFrag = "/SchoolRouteMgt/src/main/resources/templates/fragments/heading.html";
	private String menuFrag = "/SchoolRouteMgt/src/main/resources/templates/fragments/menu.html";
	private String routeFormFrag = "/SchoolRouteMgt/src/main/resources/templates/fragments/routeForm.html";
	private String uploadFormFrag = "/SchoolRouteMgt/src/main/resources/templates/fragments/uploadForm.html";
	// tons of html pages, all of them are being used.
	private String htmlFiles = "/SchoolRouteMgt/src/main/resources/templates/...html";
	// application.properties
	private String applicationProperties = "/SchoolRouteMgt/src/main/resources/application.properties";
	// file log
	private String SchoolRouteMgtLogs = "/SchoolRouteMgt/src/main/resources/SchoolRouteMgtLogs.txt";
	// application test
	private String SchoolRouteMgtTests = "/SchoolRouteMgt/src/test/java/sru/edu/SchoolRouteMgt";
	// controller tests
	private String controllerTests = "/SchoolRouteMgt/src/test/java/sru/edu/SchoolRouteMgt/controller";
	// datawriter tests
	private String datawriterTests = "/SchoolRouteMgt/src/test/java/sru/edu/SchoolRouteMgt/datawriter";
	// domain tests
	private String domainTests = "/SchoolRouteMgt/src/test/java/sru/edu/SchoolRouteMgt/domain";
	// repository tests
	private String repository = "/SchoolRouteMgt/src/test/java/sru/edu/SchoolRouteMgt/repository";
	// service tests
	private String services = "/SchoolRouteMgt/src/test/java/sru/edu/SchoolRouteMgt/services";
	// Documentation
	private String Documents = "/SchoolRouteMgt/Documents";
}
