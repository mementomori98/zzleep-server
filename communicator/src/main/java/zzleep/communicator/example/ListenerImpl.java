package zzleep.communicator.example;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

public class ListenerImpl implements Consumer<String> {

    SubjectImpl subject;

    public ListenerImpl() {
        subject = new SubjectImpl(this::receive1); // new SubjectImpl(s -> accept(s)) ### new SubjectImpl(this::receive)
    }

    @Override
    public void accept(String s) {

    }

    private void receive1(String s) {

    }

    private void receive2(String s) {

    }
}
