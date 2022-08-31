package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;

public class FlowerHolder {
    private static Flower flower;
    private static int id;

    public static Flower getFlower() {
        return flower;
    }

    public static void setFlower(Flower flower) {
        FlowerHolder.flower = flower;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        FlowerHolder.id = id;
    }
}
