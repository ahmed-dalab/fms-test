type TruckStatus = 'ACTIVE' | 'INACTIVE';

export class TruckDto {
  id: string;
  plateNumber: string;
  model: string;
  capacity: number | null;
  status: TruckStatus;
  vin: string | null;
  tenantId: string;
  createdAt: Date;
}

export class MetaDto {
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}

export class TruckListResponseDto {
  data: TruckDto[];
  meta: MetaDto;
}
