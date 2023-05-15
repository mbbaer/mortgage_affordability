package loanAmort.java;

import java.util.*;
import java.io.*;
import java.text.*;
import java.lang.*;
import java.awt.*;
public class loanAmort extends loanAmortDriver {

	public static void fileExists(java.io.File file) {
		if(!file.exists()) { //if operator - if file does not exist
			System.out.println("The file 'budget.txt' does not exist in the present working directory.\n" + //perform if file does not exist
				"Please place the file in the directory.");
			System.exit(1); //exit program if files does not exist
		}
	}
	public static int[] getBudget(java.io.File file) throws Exception {
		//define variables
		int ctr = 0; //counter to count rows in file
		int income = 0; //income to capture monthly income from file
		int all = 0; //all to capture total value from file
		int expenses = 0; //expenses to capture only the expenses form file
		int disposable = 0; //disposable to capture the disposable income from console/user
		int[] financials = new int [3]; //array to store the relevant budget items
		try { //try block
			Scanner input1 = new Scanner(file); //new scanner object input1
			Scanner input2 = new Scanner(file); //new scanner object input2
			while(input1.hasNext()) { //while loop to process lines in file
				ctr = ctr + 1; //add 1 to counter for each row containing text
				input1.next(); //more to next line
			}
			String[][] budgetItems = new String [(ctr/2)][2]; //create 2D array to store the items from file
			int[] moneyItems = new int [ctr/2]; //create 1D array to only store the monetary items from file
			for (int i = 0; i < (ctr/2); i++) { //for loop to loop through rows in file
				if (input2.hasNextLine()) {
					String line = input2.nextLine();
					String[] parts = line.split(", ", 2); //split the line into two parts
					if (parts.length != 2) {
						System.out.println("Invalid line in budget file: " + line);
						System.exit(1);
					}

					budgetItems[i][0] = parts[0]; //store the first part as the budget item description
					budgetItems[i][1] = parts[1]; //store the second part as the budget amount

					try {
						moneyItems[i] = Integer.parseInt(budgetItems[i][1]); //store only monetary items into array moneyItems
						all += moneyItems[i]; //set all = sum of all money items
					} catch (NumberFormatException e) {
						System.out.println("Invalid number in budget file: " + budgetItems[i][1]);
						System.exit(1);
					}
				}
			}
			financials[0] = income = moneyItems[0]; //set income to financials[0]
			financials[1] = expenses = all - income; //set expenses to financials[1]
			financials[2] = disposable = income - expenses; //set disposable to financials [2]
		} catch (Exception e){
			System.out.println("Exception1 occurred.");
		}
		return financials; //return financials
	}
	public static int[] loanTerms(int[] disp) throws Exception {
		Scanner input3 = new Scanner(System.in); //new scanner object input3
		boolean isInt; //define variables
		boolean isPos;
		boolean term;
		int[] loanTerms = new int[2]; //create 2D array of 2 ints to capture loan parameters
		int mortPayment = 0;
		int duration = 0;
		System.out.println("Your monthly income is: $" + disp[0]); //print income from budget array
		System.out.println("Your monthly expenses are: $" + disp[1]); //print expenses from budget array
		System.out.println("Your monthly disposable income is: $" + disp[2]); //print disposable income from budget array
		try { //try block
	        do { //do while loop to capture monthly payment value
	        	System.out.println("How much are you willing" + 
				" to spend on a monthly mortgage payment?"); //prompt user to enter monthly payment amount
	        	if (input3.hasNextInt()) { //validate user input as int
					mortPayment = input3.nextInt(); //set mortPayment to user input
					if (mortPayment > 0) { //validate user input is positive
						if (mortPayment > (disp[0] * .28)) { //validate user input is not too much
							System.out.println("Your payment amount is too high.");
							System.out.println("Generally, your mortgage payment should be no more than 28% of your monthly income.");
							System.out.println("In your case, that is $" + Math.round(disp[0] * .28) + ".");
							isInt = false;
						} else {
							isInt = true; //set to true to end loop
						}
					} else { //else if user input is not positive
						System.out.println("Mortgage payment must be a positive integer.");
						isInt = false;
					}
	        	} else { //else if user input is not an integer
	        		System.out.println("Please enter a valid integer."); 
	        		isInt = false; //set to false to continue loop
	        		input3.next(); //clear input
	        	}
	        } while(!(isInt)); //iterate loop while isInt is False
	        do { //do while loop to capture loan duration value
				System.out.println("Do you want a 15yr or 30yr mortgage?"); //prompt user to input duration
	        	if (input3.hasNextInt()) { //validate user input as int
					duration = input3.nextInt(); //set duration to user input
					if (duration == 15 || duration == 30) { //validate user input as 15 or 30
						term = true; //set to true to end loop
					} else { //else if user input not 15 or 30
						System.out.println("Mortgage term must be either 15 or 30 years.");
						term = false;
					}
	        	} else { //else if user input not int
	        		System.out.println("Please enter a valid integer."); 
	        		term = false; //set to false to continue loop
	        		input3.next(); //clear input
	        	}
	        } while(!(term)); //interate loop while term is False
		} catch (Exception e) {
			System.out.println("Exception2 occured");
		}
		loanTerms[0] = mortPayment; 
		loanTerms[1]= duration;
		return loanTerms; //return loan parameters
	}
	public static Double[] getInterestRates () throws Exception {
		String[] intRates = {"",""}; //create 2D String array
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //create DateFormat variable
		Date date = new Date(); //create new Date variable
		try{ //try block
			String URLString = "https://www.wellsfargo.com/mortgage/rates/"; //create URL variable  
			//create a File object for a text file
			java.io.File file2 = new java.io.File ("output_files" + File.separator + "MortgageText_" + dateFormat.format(date) + ".txt");
			try { //try block
				java.io.PrintWriter mortOutput = new java.io.PrintWriter(file2); //create a PrintWriter object as file2
				java.net.URL url = new java.net.URL(URLString);  //create URL object as URL variable
		        int count = 0; //counter variable
				Scanner input = new Scanner(url.openStream()); //create new Scanner object input
				while (input.hasNext()) { //while loop while input has text
					String line = input.nextLine(); //set String line variable to next line
					count += line.length(); //add 1 to counter
					mortOutput.println(line); //print URL content to mortOutput to store data
		        }
		        mortOutput.close(); //close file
		    } catch (java.net.MalformedURLException ex) { //catch block
		    	System.out.println("Invalid URL");
		    } catch (java.io.IOException ex) { //catch block
				System.out.println("I/O Errors: no such file");
		    }					
		} catch (Exception e){ //catch block
			System.out.println ("Exception3 occurred.");
		}

		Scanner sc1 = new Scanner(System.in); //create new Scanner object sc1
		String word = "%"; //create String variable to search for interest rates in text file
		//boolean flag = false; 
		//int count2 = 0; //counter variable
		String[] rates = new String[40]; //create 2D String array to store interest rates from text file
		int i = 0; //int variable to use for loop
		Double[] finalRates = new Double[2]; //create 2D array of 2 Doubles to store interest reates 
		//program user must change file path to present working directory
		Scanner sc2 = new Scanner(new FileInputStream("output_files" + File.separator + "MortgageText_" //create FileInputStream object
			+ dateFormat.format(date) + ".txt"));
		while(sc2.hasNextLine()) { //while loop while sc2 has lines
			String line = sc2.nextLine(); //store line content as line variable
			if(line.indexOf(word)!=-1) { //if line contains 'word' execute
				//flag = true;
				//count2 = count2+1; //add 1 to count2
				rates[i] = line; //store line into rates array
				i++; //add 1 to i variable
			}
		}
		//depending on interest rate line length, store rate value as substring with defined length
		if(rates[0].length() == 27 | rates[0].length() == 26){ 
			finalRates[0] = Double.parseDouble(rates[0].substring(10,14));
		} else{
			finalRates[0] = Double.parseDouble(rates[0].substring(10,13));
		} if(rates[6].length() == 27 | rates[6].length() == 26){
			finalRates[1] = Double.parseDouble(rates[6].substring(10,14));
		} else{
			finalRates[1] = Double.parseDouble(rates[6].substring(10,13));
		} return finalRates; //return rates
	}
	public static Double[] buildLoan (int[] loanParas, Double[] mortRates) {
		Double loanRate = 0.0;
		if(loanParas[1] == 15) { //if user input equals 15
			loanRate = mortRates[1]; //set loanRate to mortRates[1]
		} else { //else user input equals 30
			loanRate = mortRates[0]; //set loanRate to mortRate[0]
		}
		//set loan amount using loan arithemtic
		Double loanAmount = (((double) loanParas[0])*(12*(1-(Math.pow((1+(((double) loanRate)/100)/12),-(loanParas[1]*12))))))/(((double) loanRate)/100);
		Double[] loanBuild = new Double [3]; //create 2D array of 3 Doubles to store loan parameters
		loanBuild[0] = loanRate;
		loanBuild[1] = loanAmount;
		loanBuild[2] = (double) loanParas[1];
		return loanBuild; //return loan parameters
	}
	public static void amortTable (int[] loanParas, Double[] loanAmortVals) throws Exception {
        Double loanAmount = loanAmortVals[1]; //set loanAmount 
        Double numYears = loanAmortVals[2]; //set numYears
        double annualInterestRate = loanAmortVals[0]; //set annualInterestRate
        int monthlyPayment1 = loanParas[0]; //set monthlyPayment

        System.out.println();  // Insert a new line

        // Print the amortization schedule
        try { //try block
        	printAmortizationSchedule(loanAmount, annualInterestRate, numYears, monthlyPayment1); //call printAmortizationSchedule; pass variables
        } catch(Exception e){ //catch block
        	System.out.println("Exception");
        }
    }
    public static void printAmortizationSchedule(double principal, double annualInterestRate, double numYears, int payment) throws Exception {
        Formatter amortization; //set Formatter variable
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd"); //create new DateFormat object
		Date date2 = new Date(); //create new Date object
        double interestPaid, principalPaid, newBalance; //define variables
        double monthlyInterestRate, monthlyPayment;
        int month;
        Double numMonths = numYears * 12;

        monthlyInterestRate = annualInterestRate / 12;
        monthlyPayment      = payment;

        try{ //try block
        	amortization = new Formatter("amortization_table" + File.separator + "AmortizationTable_" + dateFormat2.format(date2) + ".txt"); //create new Formatter object
	        amortization.format("Mortgage Amount:       $ %8.2f%n", principal); //print to amortization
	        amortization.format("Mortgage Duration:  %8.2f%n", numYears);
	        amortization.format("Interest Rate:     %8.2f%%%n", annualInterestRate);
	        amortization.format("Monthly Payment:       $%8.2f%n",monthlyPayment);
	        amortization.format("Total Payment:         $ %8.2f%n", monthlyPayment * numYears * 12);

	        //print the table header
	        amortization.format("\nAmortization Schedule ");
	        for(int i = 0; i < 60; i++) {  // Draw a line
	            amortization.format("-");
	        }
	        amortization.format("\n%9s%13s%13s%13s\n",
	            "Payment #", "Interest", "Principal", "Balance");
	        amortization.format("%9s%13s%13s%13s\n\n",
	            "", "paid", "paid", "");

	        for (month = 1; month <= numMonths; month++) {
	            //compute amount paid and new balance for each payment period
	            interestPaid  = principal      * (monthlyInterestRate / 100);
	            principalPaid = monthlyPayment - interestPaid;
	            newBalance    = principal      - principalPaid;

	            //output the data item to amortization
	            amortization.format("%9d%13.2f%13.2f%13.2f\n",
           	 	month, interestPaid, principalPaid, newBalance);

	            //update the balance
	            principal = newBalance;
	        }
	    amortization.close(); //close the file
        } catch (Exception e){ //catch block
			System.out.println ("Exception4 occurred.");
		}
    }
    public static void openTable() throws Exception {
    	DateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd");
		Date date3 = new Date();
		//create new File object 
    	java.io.File file3 = new java.io.File ("amortization_table" + File.separator + "AmortizationTable_" + dateFormat3.format(date3) + ".txt");
    	try{ //try block
    		Desktop.getDesktop().open(file3); //open file to desktop
    	}catch (Exception e){
    		System.out.println("Exception5 occurred.");
    	}
    }
}