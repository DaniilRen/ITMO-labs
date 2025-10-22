class Test {
    Test returnValue() {
        return new Test();
    }
}

class TestChild extends Test {
    TestChild returnValue() {
        return new TestChild();
    }
}