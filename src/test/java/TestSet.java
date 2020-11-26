import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestSet {

	public static void main(String[] args) {
		Set set = ConcurrentHashMap.newKeySet();
		set.add(3);
		System.out.println(set.size());

	}

}
