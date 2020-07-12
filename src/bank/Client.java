package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class Client {

	private String name;
	private final static int encryptionNumber = 15;
	private int initialBalance;
	private long nid;
	
	public Client(String _name, int _initialBalance, long _nid){
		name = _name;
		initialBalance = _initialBalance;
		nid = _nid;
	}
	
	public static boolean checkPIN(int account, int PIN){
		try{
			BufferedReader dataGetter = new BufferedReader(new FileReader("src/bank/accounts.csv"));
			String row;
			String last[] = null;
			while((row = dataGetter.readLine()) != null){
				last = row.split(",");
				
				if(Integer.parseInt(last[0]) == account){
					if(PIN == Integer.parseInt(encdecPIN(last[5], false))){
						return true;
					}else{
						return false;
					}
				}
			}
		}catch(Exception e){
			
		}
		return false;
	}
	
	
	public void registerClientWithCardAccount(int balance, boolean credit, boolean card, int cardPIN){
		try {
			BufferedReader dataGetter = new BufferedReader(new FileReader("src/bank/accounts.csv"));
			String row;
			String last[] = null;
			int cardNo;
			ArrayList<String> takenNo = new ArrayList<String>();
			Random rnd = new Random();
			
			while((row = dataGetter.readLine()) != null){
				last = row.split(",");
				if(Boolean.parseBoolean(last[4]) == true){
					takenNo.add(last[6]);
				}
			}
			
			loop:
			while(true){
				cardNo = 100000 + rnd.nextInt(900000);
				for(String i : takenNo){
					if(cardNo == Integer.parseInt(i.substring(1))){
						continue loop;
					}
				}
				break;
			}
		
			dataGetter.close();
			
			FileWriter thisAcc = new FileWriter("src/bank/accounts.csv", true);
			int newAccNO = Integer.parseInt(last[0]) + 1;
			thisAcc.append(Integer.toString(newAccNO));
			thisAcc.append(",");
			thisAcc.append(name);
			thisAcc.append(",");
			thisAcc.append(Integer.toString(initialBalance));
			thisAcc.append(",");
			thisAcc.append(Boolean.toString(credit));
			thisAcc.append(",");
			thisAcc.append(Boolean.toString(card));
			thisAcc.append(",");
			thisAcc.append(encdecPIN(Integer.toString(cardPIN), true));
			thisAcc.append(",");
			thisAcc.append("c" + Integer.toString(cardNo));
			thisAcc.append(",");
			thisAcc.append(Long.toString(nid)+"\n");
		
			thisAcc.flush();
			thisAcc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	public void registerClientAccount(int balance, boolean credit){
		try {
			BufferedReader dataGetter = new BufferedReader(new FileReader("src/bank/accounts.csv"));
			String row;
			String last[] = null;
			while((row = dataGetter.readLine()) != null){
				last = row.split(",");
			}
			dataGetter.close();
			
			FileWriter thisAcc = new FileWriter("src/bank/accounts.csv", true);
			int newAccNO = Integer.parseInt(last[0]) + 1;
			thisAcc.append(Integer.toString(newAccNO));
			thisAcc.append(",");
			thisAcc.append(name);
			thisAcc.append(",");
			thisAcc.append(Integer.toString(initialBalance));
			thisAcc.append(",");
			thisAcc.append(Boolean.toString(credit));
			thisAcc.append(",");
			thisAcc.append(Boolean.toString(false));
			thisAcc.append(",");
			thisAcc.append(Long.toString(nid));
			thisAcc.append(System.lineSeparator());
			thisAcc.flush();
			thisAcc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	private static String encdecPIN(String pin, boolean encryption){
		char[] pinChar = pin.toCharArray();
		char[] encPinChar = new char[pinChar.length];
		for(int i = 0; i <= pinChar.length - 1; i++){
			int charAscii = (int)pinChar[i];
			if(encryption){
				charAscii+=encryptionNumber;
			}else{
				charAscii-=encryptionNumber;
			}
			encPinChar[i] = (char)charAscii;
		}
		String encPinStr = new String(encPinChar);
		return encPinStr;
	}
}
