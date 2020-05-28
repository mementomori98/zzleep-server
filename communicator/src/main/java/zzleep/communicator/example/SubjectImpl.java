package zzleep.communicator.example;

import java.util.function.Consumer;

public class SubjectImpl {

    // listener takes in a string
    private Consumer<String> listener;

    public SubjectImpl(Consumer<String> listener) {
        this.listener = listener;
    }

    void onText(CharSequence data) {
        // network logic
        listener.accept(data.toString());
    }

}

// interface Consumer {
//
//     void Notify(String s);
//
// }
