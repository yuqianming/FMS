package com.mvc.utils;

public class Test {
	public static void main(String[] args)
	{
		String num="2";
		switch(num)
		{
		  case "1":
			  for(int i=0;i<5;i++)
			  {
				  for(int j=0;j<5;j++)
				  {
					  if(j==4)
					  {
						  System.out.println("#");
					  }
					  else
					  {
						  if(j==2)
						  {
							  System.out.print("*");
						  }
						  else
						  {
							  System.out.print("#");
						  }
					  }

				  }
			  }
			  break;
		  case "2":
			  for(int i=0;i<5;i++)
			  {
				  for(int j=0;j<5;j++)
				  {
					  if(i%2==0)
					  {
						  if(j==4)
						  {
							  System.out.println("*");
						  }
						  else
						  {
							  System.out.print("*");
						  }
					  }
					  if(i==1)
					  {
						  if(j==4)
						  {
							  System.out.println("*");
						  }
						  else
						  {
							  System.out.print(" ");
						  }
					  }
					  if(i==3)
					  {
						  if(j==4)
						  {
							  System.out.println(" ");
						  }
						  else
						  {
							  if(j==0)
							  {
								  System.out.print("*");
							  }
							  else
							  {
								  System.out.print(" ");
							  }
						  }

					  }
				  }
			  }
			  break;
		}
	}
}
