package sample;

public class TestTest {
	
	public static void print(int i, String... abcd) {
		System.out.println(i);
		System.out.println("abcd " + abcd);
		/*if(abcd != null)
		System.out.println(abcd.length);*/
		print(3.14159,abcd);
	}
	
	public static void print(double d, String... abcd) {
		System.out.println(d);
		for(String s: abcd) {
		System.out.println(s);
		}
	}

	public static void main(String[] args) {
		print(1,"abc","nihao","chicai");
	}
}
