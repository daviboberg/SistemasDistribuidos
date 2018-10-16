package Server;

import Resources.Interest;

import java.util.ArrayList;

public class Observer {

    private ArrayList<Interest> interests;

    Observer() {
        this.interests = new ArrayList();
    }

    public void addInterest(Interest interest) {
        this.interests.add(interest);
    }

    void processEvents() {

    }
}
