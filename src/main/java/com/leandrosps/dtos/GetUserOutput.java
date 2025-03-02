package com.leandrosps.dtos;

import java.util.UUID;
public record GetUserOutput(UUID id, String firstName, String lastName, String email) {
}