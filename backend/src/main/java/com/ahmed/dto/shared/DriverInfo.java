// dto/shared/DriverInfo.java
package com.ahmed.dto.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DriverInfo {
    private Long id;
    private String driverName;
}
