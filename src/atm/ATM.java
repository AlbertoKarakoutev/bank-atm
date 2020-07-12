package atm;

import java.util.Scanner;

import bank.BankInterface;
import bank.Client;

public class ATM {

	private int _5s;
	private int _10s;
	private int _20s;
	private int _50s;
	private int _100s;
	
	Scanner sc = new Scanner(System.in);
	
	private int[] values = { 5, 10, 20, 50, 100 };
	
	public ATM(int __5s, int __10s, int __20s, int __50s, int __100s) {
		this._5s = __5s;
		this._10s = __10s;
		this._20s = __20s;
		this._50s = __50s;
		this._100s = __100s;
		
		System.out.println("Welcome!");
		System.out.println("__________________________");
		System.out.print("Enter national ID number:");
		long nid = 0;
		int account = 0;
		int pin = 0;
		try{
			nid = Long.parseLong(sc.nextLine());
			BankInterface.view(nid, true);
			System.out.print("Select account to draw from:");
			account = Integer.parseInt(sc.nextLine());
			System.out.print("Enter PIN code:");
			pin = Integer.parseInt(sc.nextLine());
		}catch(NumberFormatException e){
			System.err.println("Please try again!");
			ATM atm = new ATM(_5s, _10s, _20s, _50s, _10s);
			return;
		}
		int amount;
		if(Client.checkPIN(account, pin)){
			amount = BankInterface.funds(account, false);
			loop:
				while (true) {
					try{
						switch(availabilityCheck(amount)){
							case 0:			
								request(amount);
								break loop;
							case 2:
								System.out.println("Enter sum, divisible by 10:");
								continue loop;
							case 3:
								System.out.println("Enter sum, divisible by 20:");
								continue loop;
							case 4:
								System.out.println("Enter sum, divisible by 50:");
								continue loop;
							case 5:
								System.out.println("Enter sum, divisible by 100:");
								continue loop;
							case 6:
								System.out.println("No funds in machine!");
								break loop;
						}
					} catch(Exception e) {
						System.err.println("Invalid request! Please enter new request: \n");
						continue loop;
					}
				}
		}else{
			System.out.println("Wrong PIN!");
		}	
	}

	public void request(int amount) {
		
		int[] amounts = { _5s, _10s, _20s, _50s, _100s };
		int[] amtsGiven = new int[5];
		int sum = 0;
		
		loop:
		for (int i = amounts.length - 1; i >= 0; i--) {
			while (sum + values[i] <= amount) {
				if(amounts[i]-1 >= 0){
					sum += values[i];
					amounts[i]--;
					amtsGiven[i]++;	
				}else if(i == 0){
					System.err.println("Can not supply selected amount!");
					return;
				}else{
					continue loop;
				}
			}		
		}
		
		for (int i = 0; i < amtsGiven.length; i++) {
			if(amtsGiven[i]!=0){
				System.out.println("Giving " + amtsGiven[i] + "x" + values[i] + "$");
			}
		}
		
		System.out.println("= " + sum);
	}
	
	public int availabilityCheck(int amount){
		int atmFunds = 0;
		int[] amounts = { _5s, _10s, _20s, _50s, _100s };
		for(int i = 0; i < 5; i++){
			atmFunds+=amounts[i]*values[i];
			if(amounts[i] == 0 && amounts[i+1] != 0){
				if(amount%values[i+1]==0){
					return 0;
				}
				return i+2;
			}
		}
		
		if(amount > atmFunds){
			System.err.println("Not enough funds in ATM!");
			return 1;
		}
		return 0;
	}

}
