type TruckStatus = 'ACTIVE' | 'INACTIVE';
type TripStatus = 'PENDING' | 'ONGOING' | 'COMPLETED'; // Example

export class DriverDto {
  id: string;
  licenseNumber: string;
  status: TruckStatus;
  phone: string | null;
  tenantId: string;
  createdAt: Date;
  user: UserDto;
  trips: TripDto[];
}

class UserDto {
  id: string;
  name: string;
  email: string;
  role: string;
}

class TripDto {
  id: string;
  origin: string;
  destination: string;
  price: number;
  status: TripStatus;
  startTime: Date | null;
  endTime: Date | null;
}

class MetaDto {
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}
