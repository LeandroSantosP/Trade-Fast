package com.leandrosps;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(3001);
        get("/:name", (req, res) -> {
            var value = req.params(":name");
            return "Hello World! " + value;
        });
    }
}