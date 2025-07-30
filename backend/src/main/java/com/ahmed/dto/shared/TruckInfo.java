// dto/shared/TruckInfo.java
package com.ahmed.dto.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TruckInfo {
    private Long id;
    private String truckModel;
}
