import { ConflictException } from '@nestjs/common';

export class DuplicateEntryException extends ConflictException {
  constructor(field: string) {
    super(`A record with the same ${field} already exists.`);
  }
}
