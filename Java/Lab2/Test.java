class Test {
    Test returnValue() {
        return this;
    }
}

class TestChild extends Test {
    @Override
    TestChild returnValue() {
        return new TestChild();
    }
}





















// import java.util.List;

// public static void foo(List<? extends Test> list) {}

// List<TestChild> list1 = new ArrayList<>();
// List<Test> list2 = new ArrayList<>();

// foo(list1) // OK
// foo(list2) // OK



// public static void foo(List<? super TestChild> list) {}

// List<TestChild> list1 = new ArrayList<>();
// List<Test> list2 = new ArrayList<>();

// foo(list1) // OK
// foo(list2) // OK


// public static void foo(List<TestChild> list) {}

// List<TestChild> list1 = new ArrayList<>();
// List<Test> list2 = new ArrayList<>();

// foo(list1) // OK
// foo(list2) // Incompatile types