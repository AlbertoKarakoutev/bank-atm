package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import atm.ATM;

public class BankInterface {
	
	public BankInterface(){
		System.out.println("Loading interface...");
		System.out.println("Starting...");
		System.out.println("Welcome!");
		while(true){
			System.out.println("______________________________________________");
			System.out.println("|Enter one of the following commands:         |");
			System.out.println("|\t [view]      - View client Information|");
			System.out.println("|\t [register]  - Register new client    |");
			System.out.println("|\t [add]       - Top up client account  |");
			System.out.println("|\t [draw]      - Draw funds             |");
			System.out.println("|\t [close]     - Close client account   |");
			System.out.println("|\t [atm]       - Open ATM interface     |");
			System.out.println("|_____________________________________________|");
			getInput();
		}
	}

	private void getInput() {
		System.err.print("Command:");
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter(System.lineSeparator());
		String input = sc.next();
		switch(input){
			case "view":
				System.out.print("Enter national ID number:");
				Scanner scv = new Scanner(System.in);
				long nid = Long.parseLong(scv.nextLine());
				view(nid, false);
				break;
			case "register":
				register();
				break;
			case "add":
				System.out.print("Enter account number:");	
				Scanner sca = new Scanner(System.in);
				int accountNo = Integer.parseInt(sca.nextLine());
				sca.reset();
				System.out.print("Amount to add:");
				int funda = Integer.parseInt(sc.nextLine());
				sca.reset();
				funds(accountNo, funda, true, false);
				break;
			case "draw":
				System.out.print("Enter account number:");	
				Scanner scd = new Scanner(System.in);
				int accountNum = Integer.parseInt(scd.nextLine());
				scd.reset();
				System.out.print("Amount to draw:");
				int fundd = Integer.parseInt(scd.nextLine());
				scd.reset();
				if(funds(accountNum, fundd, false, true)){
					System.out.println("Operaion succesful!");
				}
				break;
			case "close":
				close();
				break;
			case "atm":
				new ATM(20, 20, 20, 20, 20);
				break;
			default:
				System.err.println("Please enter valid command!");
				break;
		}
	}
	
	public static ArrayList<String[]> clientFinder(long nid){
		ArrayList<String[]> clientAccounts = new ArrayList<String[]>();
		 try{
				BufferedReader dataGetter = new BufferedReader(new FileReader("src/bank/accounts.csv"));
				String row;
				String last[] = new String[8];
				while((row = dataGetter.readLine()) != null){	
					last = row.split(",");
					if(Boolean.parseBoolean(last[4])){
						if(nid == Long.parseLong(last[7])){
							clientAccounts.add(last);
						}
					}else{
						if(nid == Long.parseLong(last[5])){
							clientAccounts.add(last);
						}
					}
				}
				dataGetter.close();
				if(clientAccounts.size()>0){
					return clientAccounts;
				}else{
					System.out.println("No client accounts under this national ID number!");
					return null;
				}
		 }catch(Exception e){
			 System.out.println("Error");
			 e.printStackTrace();
		 }
		 return null;
	}
	
	public static void view(Long nid, boolean onlyCardAccounts){
		
		if(clientFinder(nid)!=null){
			System.out.println("___________________");
			System.out.println("Name: " + clientFinder(nid).get(0)[1]);
			System.out.println(" ");
			for(String[] s : clientFinder(nid)){
				if(onlyCardAccounts){
					if(Boolean.parseBoolean(s[4])){
						System.out.println("Account #" + s[0]);
						System.out.println("Balance: " + s[2]);
						if(Boolean.parseBoolean(s[3])){
							System.out.println("Type: Credit");
						}else{
							System.out.println("Type: Debit");
						}
						System.out.println("Card: Yes");
						System.out.println("Card #" + s[6]);
						System.out.println(" ");
					}
				}else{
					System.out.println("Account #" + s[0]);
					System.out.println("Balance: " + s[2]);
					if(Boolean.parseBoolean(s[3])){
						System.out.println("Type: Credit");
					}else{
						System.out.println("Type: Debit");
					}
					if(Boolean.parseBoolean(s[4])){
						System.out.println("Card: Yes");
						System.out.println("Card #" + s[6]);
					}else{
						System.out.println("Card: No");
					}
					System.out.println(" ");
				}
			}
			System.out.println("___________________");
		}else{
			System.err.println("No client found under this national ID number!");
		}
	}
	
	private void register(){
		System.out.print("Enter client name:");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		
		int balance;
		long nid;
		try{
			System.out.print("Enter initial balance:");
			balance = Integer.parseInt(sc.nextLine());
			System.out.print("Enter client national ID number:");
			nid = Long.parseLong(sc.nextLine());
		}catch(NumberFormatException e){
			System.err.println("Please enter a number!");
			register();
			return;
		}
		Client c = new Client(name, balance, nid);
		System.out.print("Credit or debit account? C/D");
		String cd = sc.nextLine();
		System.out.print("Card account? Y/N");
		String yn = sc.nextLine();
		if(yn.toLowerCase().equals("y")){
			System.out.print("Enter card PIN:");
			int pin = 0;
			try{
				pin = Integer.parseInt(sc.nextLine());
			}catch(NumberFormatException e){
				System.err.println("Please enter a number:");
				try{
					pin = Integer.parseInt(sc.nextLine());
				}catch(NumberFormatException e1){
					System.err.println("Invalid PIN number!");
					return;
				}
			}
			if(cd.toLowerCase().equals("c")){
				c.registerClientWithCardAccount(balance, true, true, pin);
			}else{
				c.registerClientWithCardAccount(balance, false, true, pin);
			}
		}else{
			if(cd.toLowerCase().equals("c")){
				c.registerClientAccount(balance, true);
			}else{
				c.registerClientAccount(balance, false);
			}
		}
		view(nid, false);
	}
	
	public static boolean funds(int accountNo, int fund, boolean adding, boolean fix){
		Scanner sc = new Scanner(System.in);
		try {
			BufferedReader accountFinder = new BufferedReader(new FileReader("src/bank/accounts.csv"));
			String row;
			String[] words;
			
			ArrayList<String[]> data = new ArrayList<String[]>();
			while((row = accountFinder.readLine()) != null){
				words = row.split(",");
				data.add(words);
			}
			accountFinder.close();
			for(int i = 0; i < data.size(); i++){
				if(accountNo == Integer.parseInt(data.get(i)[0])){
					if(!fix){
						System.out.println("Name: " + data.get(i)[1]);
						System.out.println(" ");
						System.out.println("Account #" + data.get(i)[0]);
						System.out.println("Balance: " + data.get(i)[2]);
						if(Boolean.parseBoolean(data.get(i)[3])){
							System.out.println("Type: Credit");
						}else{
							System.out.println("Type: Debit");
						}
						if(Boolean.parseBoolean(data.get(i)[4])){
							System.out.println("Card: Yes");
							System.out.println("Card #" + data.get(i)[6]);
						}
						else{
							System.out.println("Card: No");
						}
						System.out.println(" ");
					}
					if(adding){			
						data.get(i)[2] = Integer.toString(Integer.parseInt(data.get(i)[2])+fund);
						if(!fix)System.out.println("New balance is " + data.get(i)[2]);
					}else{
						if(Integer.parseInt(data.get(i)[2]) - fund >= 0){
							data.get(i)[2] = Integer.toString(Integer.parseInt(data.get(i)[2])-fund);
						}else{
							System.out.println("Insufficient funds!");
							return false;
						}
					}
				}
			}
			FileWriter thisAcc = new FileWriter("src/bank/accounts.csv");
			thisAcc.close();
			FileWriter append = new FileWriter("src/bank/accounts.csv", true);
			for(int i = 0; i < data.size(); i++){
				append.append(data.get(i)[0]);
				append.append(",");
				append.append(data.get(i)[1]);
				append.append(",");
				append.append(data.get(i)[2]);
				append.append(",");
				append.append(data.get(i)[3]);
				append.append(",");
				append.append(data.get(i)[4]);
				append.append(",");
				append.append(data.get(i)[5]);
				if(data.get(i).length>6){
					append.append(",");
					append.append(data.get(i)[6]);
					append.append(",");
					append.append(data.get(i)[7]);
				}
				append.append(System.lineSeparator());
				
			}
			append.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void close(){
		System.out.print("Enter account number:");	
		Scanner sc = new Scanner(System.in);
		int accountNo = Integer.parseInt(sc.nextLine());
		sc.reset();
		
		try {
			BufferedReader accountFinder = new BufferedReader(new FileReader("src/bank/accounts.csv"));
			String row;
			String[] words;
			ArrayList<String[]> data = new ArrayList<String[]>();
			while((row = accountFinder.readLine()) != null){
				words = row.split(",");
				data.add(words);
			}
			accountFinder.close();
			mainLoop:
			for(int i = 0; i < data.size(); i++){
				if(accountNo == Integer.parseInt(data.get(i)[0])){
					
					System.out.println("Name: " + data.get(i)[1]);
					System.out.println(" ");
					System.out.println("Account #" + data.get(i)[0]);
					System.out.println("Balance: " + data.get(i)[2]);
					if(Boolean.parseBoolean(data.get(i)[3])){
						System.out.println("Type: Credit");
					}else{
						System.out.println("Type: Debit");
					}
					if(Boolean.parseBoolean(data.get(i)[4])){
						System.out.println("Card: Yes");
						System.out.println("Card #" + data.get(i)[6]);
					}
					else{
						System.out.println("Card: No");
					}
					System.out.println(" ");
					
					System.out.print("Are you sure you want to cose this account? Y/N");
					String yesno = sc.nextLine();
					sc.reset();
					if(yesno.equals("y")){
						if(Integer.parseInt(data.get(i)[2])>0){
							System.out.print("Select account to transfer the balance:");
							String newAcc = sc.nextLine();
							for(int j = 0; j < data.size(); j++){
								if(newAcc.equals(data.get(j)[0])){
									data.get(j)[2] = Integer.toString(Integer.parseInt(data.get(i)[2])+Integer.parseInt(data.get(j)[2]));
									System.out.println("Transfer complete!");
									data.remove(i);	
									break mainLoop;
								}
								
							}
							System.out.println("Account not found!");
							return;	
						}
					}
				}
			}
			FileWriter thisAcc = new FileWriter("src/bank/accounts.csv");
			thisAcc.close();
			FileWriter append = new FileWriter("src/bank/accounts.csv", true);
			for(int i = 0; i < data.size(); i++){
				append.append(data.get(i)[0]);
				append.append(",");
				append.append(data.get(i)[1]);
				append.append(",");
				append.append(data.get(i)[2]);
				append.append(",");
				append.append(data.get(i)[3]);
				append.append(",");
				append.append(data.get(i)[4]);
				append.append(",");
				append.append(data.get(i)[5]);
				if(data.get(i).length>6){
					append.append(",");
					append.append(data.get(i)[6]);
					append.append(",");
					append.append(data.get(i)[7]);
				}
				append.append(System.lineSeparator());
				
			}
			append.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}
	

