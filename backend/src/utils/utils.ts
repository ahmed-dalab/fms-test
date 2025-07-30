import { QueryDriverDto } from 'src/driver/dto/query-driver.dto';
import { TenantQueryDTO } from 'src/tenants/dto/tenant-query.dto';
import { QueryTrucksDto } from 'src/trucks/dto/query-trucks.dto';
import { UserQueryDTO } from 'src/users/dto/user-query.dto';

export function isUUID(str: string): boolean {
  const uuidRegex =
    /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
  return uuidRegex.test(str);
}
export function buildTruckFilterQuery(query: QueryTrucksDto) {
  const where: any = {};
  if (query.status) {
    where.status = query.status.toUpperCase();
  }
  if (query.plateNumber) {
    where.plateNumber = {
      contains: query.plateNumber,
      mode: 'insensitive',
    };
  }
  return where;
}
export function buildTenantFilterQuery(query: TenantQueryDTO) {
  const where: any = {};
  if (query.email) {
    where.email = query.email.toLocaleLowerCase();
  }
  if (query.city) {
    where.city = {
      contains: query.city,
      mode: 'insensitive',
    };
  }
  return where;
}
export function buildUserFilterQuery(query: UserQueryDTO) {
  const where: any = {};
  if (query.email) {
    where.email = query.email.toLocaleLowerCase();
  }
  if (query.role) {
    where.role = query.role.toUpperCase(); // remove contains/mode
  }
  return where;
}

export function buildDriverFilterQuery(query: QueryDriverDto) {
  const where: any = {};
  if (query.status) {
    where.status = query.status.toUpperCase();
  }
  if (query.licenseNumber) {
    where.licenseNumber = {
      contains: query.licenseNumber,
      mode: 'insensitive',
    };
  }
  return where;
}

export function mapPrismaDuplicateFields(fields: string[]) {
  return fields
    .map((field) => {
      switch (field) {
        case 'plateNumber':
          return 'Plate Number';
        case 'vin':
          return 'VIN';
        default:
          return field;
      }
    })
    .join(', ');
}

export function cleanNulls<T extends Record<string, any>>(obj: T): Partial<T> {
  const cleaned: Partial<T> = {};
  for (const key in obj) {
    if (obj[key] !== null) {
      cleaned[key] = obj[key];
    }
  }
  return cleaned;
}
