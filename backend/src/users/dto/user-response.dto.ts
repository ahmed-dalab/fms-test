import { IsString, IsEmail } from 'class-validator';

class UserResponseDTO {
  @IsString()
  id: string;

  @IsString()
  name: string;

  @IsEmail()
  email: string;

  @IsString()
  role: string;

  @IsString()
  tenantId: string | null;
}

export class UserListResponseDTO {
  data: UserResponseDTO[];
  meta: {
    total: number;
    page: number;
    limit: number;
    totalPages: number;
  };
}
