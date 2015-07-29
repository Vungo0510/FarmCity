package farmcity.ui;

import java.io.*;
import java.util.*;

import farmcity.entity.*;
import farmcity.controller.*;
import farmcity.exception.*;

/**
 * This UI represents the Friend Menu of the game, which is displayed 
 * after the player choose 'My Friend' function in the main menu. This UI
 * consists of functions for player to manage his friend list (view, send request(s),
 * accept/ reject request(s) or remove a friend).   
 * @author G3-T12
 */
public class FriendUI {

    private ManageFriendController manageFriendController;
    private Player player;
	
	/**
	 * This constructor initializes manageFriendController by passing in the object
	 * which represents the current Player
	 * @param player The current player
	 */
    public FriendUI(Player player) {
        this.player = player;
		try {
			manageFriendController = new ManageFriendController();
		} catch (CityException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }
	
	
	/**
	 * This method executes the Friend Menu and prints out the user's friend list
	 * to user's screen for him/ her to choose the function that he/she wants 
	 * to proceed with. The method then proceed to call the 
	 * corresponding function or go back to the main menu according to user's input.
	 */
    public void execute() {
        Scanner sc = new Scanner(System.in);
        String username = player.getUsername();

        String userInput = " ";
        char option = ' ';

        do {
                
            System.out.println("== Farm City :: My Friends ==");
            System.out.println("Welcome, " + player.getFullName());
            System.out.println();
            System.out.print("My Friends: ");
            HashMap<Player,Character> friendListMap = manageFriendController.retrievePlayerFriends(player.getUsername());
   
            int friendCount = 0;
            int requestCount = 0;
            int totalCount = 0;

            HashMap<Integer, Player> friendMap = new HashMap<Integer, Player>();

            System.out.println("My Friends: ");

            Iterator<Player> friendListMapIter = friendListMap.keySet().iterator();

            while(friendListMapIter.hasNext()) {

                Player  friend = friendListMapIter.next();
                char status = friendListMap.get(friend);

                if (status == 'f') {
                    friendCount++;
                    totalCount++;
                    friendMap.put(totalCount, friend);
                    System.out.println(totalCount + "." + friend.getUsername());

                }
            }

            System.out.println("My Requests: ");

             Iterator<Player> requestListMapIter = friendListMap.keySet().iterator();

            while(requestListMapIter.hasNext()) {
                Player request = requestListMapIter.next();
                char status = friendListMap.get(request);

                if (status == 'r') {
                    requestCount++;
                    totalCount++;
                    friendMap.put(totalCount, request);
                    System.out.println(totalCount + "." + request.getUsername());

                }
            }

            System.out.println();

            System.out.print("[M]ain | [U]nfriend | re[Q]uest | [A]ccept | [R]eject > ");
            userInput = sc.nextLine().toUpperCase();

            int choice = -1;
			boolean validInput = false;
			
			try {
				if (userInput.length() >= 1) {
					
					option = userInput.toUpperCase().charAt(0);

					if (userInput.length() == 1 && option == 'Q') {
						validInput = true;
					}

					if (userInput.length() >= 2 && (option == 'A' || option == 'U' || option == 'R')) {
						choice = Integer.parseInt(userInput.substring(1,userInput.length()));
					}
				}
            // request --> from friendRequests
                switch (option) {
                    case 'Q':
                        processRequest();
                        break;
                    case 'U':
                        processUnfriend(friendMap, friendCount, choice);
                        break;
                    case 'A':
                        processAccept(friendMap, friendCount, choice);
                        break;
                    case 'R':
                        processReject(friendMap, friendCount, choice);
                        break;
                    case 'M':
                        break;
                    default:
                        System.out.println("Please enter a valid choice!");
                        break;
                }
            } catch (CityException e){
                System.out.println(e.getMessage());
            }
			System.out.println();
        } while (option != 'M');

    }
	
	
	/**
	 * This method allows a user to send a friend request to another player of his/ her
	 * choice according to user's input by proceed to the requestFriend method of
	 * ManageFriendController
	 * @throws CityException If the send friend request process is unsuccessful, throws CityException
	 */
    public void processRequest() throws CityException {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the username > ");
        String friendUsername = sc.nextLine();

 
        manageFriendController.requestFriend(player, friendUsername);
        
        System.out.println("You have sent " + friendUsername + " a request");
    }
	
	/**
	 * This method allows a user to accept a friend request from another player
	 * by taking in the choice which corresponds to the request he/she wants to accept
	 * and proceed to the acceptFriend method of ManageFriendController
	 * @param friendMap the HashMap that contains pairs of friends and requests
     * @param friendCount the number that represent the request in the player's Friend UI
     * @param choice The request that user chooses to accept
     * @throws CityException If the accept friend process is unsuccessful, throws CityException
	 */
    public void processAccept(HashMap<Integer, Player> friendMap, int friendCount, int choice) throws CityException {
        if (friendCount < choice && choice <= friendMap.size()) {
            Player request = friendMap.get(choice);	
			String requestUsername = request.getUsername();
            manageFriendController.acceptFriend(player, requestUsername);
			
            System.out.println(requestUsername + " is now your friend.");
        } else {
            System.out.println("Please ensure that you entered a valid input!");
        }
    }
	
	/**
	 * This method allows a user to reject a friend request from another player
	 * by taking in the choice which corresponds to the request he/she wants to reject
	 * and proceed to the acceptFriend method of ManageFriendController
	 * @param friendMap the HashMap that contains pairs of friends and requests
     * @param friendCount the number that represent the request in the player's Friend UI
     * @param choice The request that user chooses to reject
     * @throws CityException If the reject friend process is unsuccessful, throws CityException
	 */
    public void processReject(HashMap<Integer, Player> friendMap, int friendCount, int choice) throws CityException {
        if (friendCount < choice && choice <= friendMap.size()) {
            Player rejectedFriend = friendMap.get(choice);
			String rejectedUsername = rejectedFriend.getUsername();
            manageFriendController.rejectFriend(player.getUsername(), rejectedUsername);
            System.out.println("You have rejected the request of " + rejectedUsername);

        } else {
            System.out.println("Invalid option!");
        }
    }
	
	/**
	 * This method allows a user to remove a friend from his friend list
	 * by taking in the choice which corresponds to the friend he/she wants to remove
	 * and proceed to the unfriend method of ManageFriendController
	 * @param friendMap the HashMap that contains pairs of friends and requests
     * @param friendCount the number that represent the friend in the player's Friend UI
     * @param choice The player that user chooses to remove from his/ her friend list
     * @throws CityException If the reject friend process is unsuccessful, throws CityException
	 */
    public void processUnfriend(HashMap<Integer, Player> friendMap, int friendCount, int choice) throws CityException {
        if (friendCount > 0 && choice >= 1 && choice <= friendCount) {
            Player friend = friendMap.get(choice);
			String friendUsername = friend.getUsername();
            manageFriendController.unfriend(player.getUsername(),friendUsername);

            System.out.println("You have successfully unfriended " + friendUsername);

        } else {
            System.out.println("Please ensure that you entered a valid input!");
        }
    }
}
