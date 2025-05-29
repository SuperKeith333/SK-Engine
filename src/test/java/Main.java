public class Main {
    public static void main(String args[]) {
        TestApp app = new TestApp();

        app.setTitle("Test");
        app.setFullscreen(false);
        app.setWindowDimensions(320, 240);

        app.begin();
    }
}
