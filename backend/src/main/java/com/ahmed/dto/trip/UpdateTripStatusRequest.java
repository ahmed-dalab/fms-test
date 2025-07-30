// com.ahmed.payload.request.UpdateTripStatusRequest.java
package com.ahmed.dto.trip;

import com.ahmed.model.Trip;

public class UpdateTripStatusRequest {
    private Trip.Status status;

    public Trip.Status getStatus() {
        return status;
    }

    public void setStatus(Trip.Status status) {
        this.status = status;
    }
}
