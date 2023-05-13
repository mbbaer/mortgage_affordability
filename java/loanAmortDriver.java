/*
This is the driver program that will create a loan amortization schedule using the user-provided budget.
	The program will evaluate the user budget, then ask for an amount of disposable income the user
			is comfortable spending on a monthly mortgage payment. This value represents the monthly payment amount.
	Once the montly payment amount is entered, the user must select between a 15yr or 30yr mortgage. This value represents the loan duration. 
	The program then searches a URL to get the current mortgage rates. The program stores a value based on the loan duration. 
		This stored value represents the annual interest rate.
	Once the monthly payment amount, the loan duration, and the annual interest rate are determined, 
		the program program builds a loan amortization schedule using the parameters.
	The program then prints the loan parameters and amortization schedule to a text file that is opened after execution is complete. 

Assumptions:
1. program user must place loanAmort.java into the present working directory
2. program user must place loanAmortDriver.java into the present working directory
3. program user must place loanAmort package folder into the present working directory
4. program user must place budget.txt into the present working directory
5. user can add items to budget.txt, but cannot change format. Ex: [budgetItem], [monetary amount]; e.g. dogfood, 50
	a. no spaces can be used for budgetItems Ex: dogfood, NOT dog food
	b. row1 of budget.txt must be income value


*/
package loanAmort.java; //add class loanAmortDriver to loanAmort package

public class loanAmortDriver{
	//program user must place budget.txt in present working directory
	//keep format the same for each new row "[item], amount"
	public static java.io.File file = new java.io.File("./budget/budget.txt"); //create new File object to get budget data
	public static int[] disp = new int[3]; //create array with 3 ints to store budget items
	public static int[] loanParas = new int[2]; //create array with 2 ints to store loan parameters
	public static Double[] mortRates = new Double[2]; //create array with 2 doubles to store 15yr & 30yr rates
	public static Double[] loanAmortVals = new Double[3]; //create array with 3 doubles to store loan amount, interest rate, duration
	
	public static void main(String[] args) throws Exception { //main driver method
		loanAmort.fileExists(file); //check if the file exists in pwd
		disp = loanAmort.getBudget(file); //get budget items from file and store into disp array
		loanParas = loanAmort.loanTerms(disp); //promot user to enter loan terms
		mortRates = loanAmort.getInterestRates(); //get live interest rates from internet URL
		loanAmortVals = loanAmort.buildLoan(loanParas, mortRates); //get all final loan parameters
		loanAmort.amortTable(loanParas, loanAmortVals); //build loan amortization schedule
		loanAmort.openTable(); //display schedule to desktop
	}
}