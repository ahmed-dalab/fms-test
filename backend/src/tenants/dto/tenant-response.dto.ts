export class TenantKDto {
  id: string;
  name: string;
  email: string;
  phoneNumber: string;
  logoUrl: string | null;
  address: string | null;
  city: string | null;
  state: string | null;
  zipCode: string | null;
  industry: string | null;
  companySize: number | null;
  createdAt: Date;
  updatedAt: Date;
}

export class MetaDto {
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}

export class TenantListResponseDto {
  data: TenantKDto[];
  meta: MetaDto;
}
