package edu.rice.comp610.controller;

import edu.rice.comp610.model.Rating;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.BadRequestException;

import java.util.UUID;

public interface RatingManager {
    void updateRating(Rating rating) throws BadRequestException, DatabaseException;
    Double getRating(UUID userId) throws DatabaseException;
}
