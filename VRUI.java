import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VRUI extends Customer {
	private static Scanner scanner = new Scanner(System.in) ;
	private List<Customer> customers = new ArrayList<Customer>() ;
	private List<Video> videos = new ArrayList<Video>() ;

	public VRUI() {
		super();
	}

	public static void main(String[] args) {
		VRUI ui = new VRUI() ;

		boolean quit = false ;
		while ( ! quit ) {
			int command = ui.showCommand() ;
			switch ( command ) {
				case 0: quit = true ; break ; // if enter 0, this work is finished
				case 1: ui.listCustomers() ; break ; // print list of customers
				case 2: ui.listVideos() ; break ; // print list of videos
				case 3: ui.register("customer") ; break ; // register customer or video, this is separate by condition(if-else)
				case 4: ui.register("video") ; break ;
				case 5: ui.rentVideo() ; break ; // enter customer, video then setRent "true"
				case 6: ui.returnVideo() ; break ; // enter customer, video then setRent "false"
				case 7: ui.getCustomerReport() ; break; // print reports of customers
				case 8: ui.clearRentals() ; break ; // enter customer then clear rental history of customer
				// smell: should be initialized at the beginning of main.
				case -1: ui.init() ; break ;
				default: break ;
			}
		}
		System.out.println("Bye");
	}

	public void clearRentals() {
		System.out.println("Enter customer name: ") ;
		String customerName = scanner.next() ;

		Customer foundCustomer = null ;
		for ( Customer customer: customers ) {
			if ( customer.getName().equals(customerName)) {
				foundCustomer = customer ;
				break ;
			}
		}

		if ( foundCustomer == null ) {
			System.out.println("No customer found") ;
		} else {
			System.out.println("Name: " + foundCustomer.getName() +
					"\tRentals: " + foundCustomer.getRentals().size()) ;
			for ( Rental rental: foundCustomer.getRentals() ) {
				System.out.print("\tTitle: " + rental.getVideo().getTitle() + " ") ;
				System.out.print("\tPrice Code: " + rental.getVideo().getPriceCode()) ;
			}

			List<Rental> rentals = new ArrayList<Rental>() ;
			foundCustomer.setRentals(rentals);
		}
	}

	public void returnVideo() {
		System.out.println("Enter customer name: ") ;
		String customerName = scanner.next() ;

		Customer foundCustomer = null ;
		for ( Customer customer: customers ) {
			if ( customer.getName().equals(customerName)) {
				foundCustomer = customer ;
				break ;
			}
		}
		if ( foundCustomer == null ) return ;

		System.out.println("Enter video title to return: ") ;
		String videoTitle = scanner.next() ;

		List<Rental> customerRentals = foundCustomer.getRentals() ;
		for ( Rental rental: customerRentals ) {
			// videoTitleChecker()
			if ( rental.getVideo().getTitle().equals(videoTitle) && rental.getVideo().isRented() ) {
				rental.returnVideo();
				rental.getVideo().setRented(false);
				break ;
			}
		}
	}

	private void init() {
		Customer james = new Customer("James") ;
		Customer brown = new Customer("Brown") ;
		customers.add(james) ;
		customers.add(brown) ;

		Video v1 = new Video("v1", Video.CD, Video.REGULAR, new Date()) ;
		Video v2 = new Video("v2", Video.DVD, Video.NEW_RELEASE, new Date()) ;
		videos.add(v1) ;
		videos.add(v2) ;

		Rental r1 = new Rental(v1) ;
		Rental r2 = new Rental(v2) ;

		james.addRental(r1) ;
		james.addRental(r2) ;
	}

	public void listVideos() {
		System.out.println("List of videos");

		for ( Video video: videos ) {
			System.out.println("Price code: " + video.getPriceCode() +"\tTitle: " + video.getTitle()) ;
		}
		System.out.println("End of list");
	}

	public void listCustomers() {
		System.out.println("List of customers");
		for ( Customer customer: customers ) {
			System.out.println("Name: " + customer.getName() +
					"\tRentals: " + customer.getRentals().size()) ;
			for ( Rental rental: customer.getRentals() ) {
				System.out.print("\tTitle: " + rental.getVideo().getTitle() + " ") ;
				System.out.print("\tPrice Code: " + rental.getVideo().getPriceCode()) ;
			}
		}
		System.out.println("End of list");
	}

	public void getCustomerReport() {
		System.out.println("Enter customer name: ") ;
		String customerName = scanner.next() ;

		Customer foundCustomer = null ;
		for ( Customer customer: customers ) {
			if ( customer.getName().equals(customerName)) {
				foundCustomer = customer ;
				break ;
			}
		}

		if ( foundCustomer == null ) {
			System.out.println("No customer found") ;
		} else {
			String result = foundCustomer.getReport() ;
			System.out.println(result);
		}
	}

	public void rentVideo() {
		System.out.println("Enter customer name: ") ; // enter name of customer
		String customerName = scanner.next() ;

		Customer foundCustomer = null ; // find name from the customer_list
		for ( Customer customer: customers ) {
			if ( customer.getName().equals(customerName)) {
				foundCustomer = customer ;
				break ;
			}
		}

		if ( foundCustomer == null ) return ; // if there is no customer same with the name, just return ;

		System.out.println("Enter video title to rent: ") ; // if there is a customer same with the name, enter name of video
		String videoTitle = scanner.next() ;

		Video foundVideo = null ;// find name from the video list
		for ( Video video: videos ) {
			if ( video.getTitle().equals(videoTitle) && video.isRented() == false ) {
				foundVideo = video ;
				break ;
			}
		}

		if ( foundVideo == null ) return ; // if there is no video same with the name, just return ;

		Rental rental = new Rental(foundVideo) ; // if there is a video same with the name, set rented -> true
		foundVideo.setRented(true);

		// set에 add, set하는 것은 get으로 가져와서 쓰는게 아니라 wrapper로 쓰는게 좋음
		List<Rental> customerRentals = foundCustomer.getRentals() ;
		customerRentals.add(rental);
		foundCustomer.setRentals(customerRentals);
	}

	// smell
	// this register(...) is separated process by condition(if-else).
	// so new object is added, this conditions will be increased then it will be violated OCP(Open Closed Principle)
	// but actually customer/video are not same type, just use registerVideo() and registerCustomer()
	// smell: long method(use registerVideo() and registerCustomer())
	public void register(String object) {
		if ( object.equals("customer") ) { // add new customer
			System.out.println("Enter customer name: ") ;
			String name = scanner.next();
			Customer customer = new Customer(name) ;
			customers.add(customer) ;
		}
		else { // add new video
			System.out.println("Enter video title to register: ") ;
			String title = scanner.next() ;

			System.out.println("Enter video type( 1 for VHD, 2 for CD, 3 for DVD ):") ;
			int videoType = scanner.nextInt();

			System.out.println("Enter price code( 1 for Regular, 2 for New Release ):") ;
			int priceCode = scanner.nextInt();

			Date registeredDate = new Date();
			Video video = new Video(title, videoType, priceCode, registeredDate) ;
			videos.add(video) ;
		}
	}

	public int showCommand() {
		System.out.println("\nSelect a command !");
		System.out.println("\t 0. Quit");
		System.out.println("\t 1. List customers");
		System.out.println("\t 2. List videos");
		System.out.println("\t 3. Register customer");
		System.out.println("\t 4. Register video");
		System.out.println("\t 5. Rent video");
		System.out.println("\t 6. Return video");
		System.out.println("\t 7. Show customer report");
		System.out.println("\t 8. Show customer and clear rentals");

		int command = scanner.nextInt() ;

		return command ;

	}
}
