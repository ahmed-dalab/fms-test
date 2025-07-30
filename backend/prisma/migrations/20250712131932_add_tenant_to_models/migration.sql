/*
  Warnings:

  - Added the required column `tenantId` to the `Driver` table without a default value. This is not possible if the table is not empty.
  - Added the required column `tenantId` to the `Expense` table without a default value. This is not possible if the table is not empty.
  - Added the required column `tenantId` to the `FuelRecord` table without a default value. This is not possible if the table is not empty.
  - Added the required column `tenantId` to the `MaintenanceRecord` table without a default value. This is not possible if the table is not empty.
  - Added the required column `tenantId` to the `Trip` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE "Driver" ADD COLUMN     "tenantId" UUID NOT NULL;

-- AlterTable
ALTER TABLE "Expense" ADD COLUMN     "tenantId" UUID NOT NULL;

-- AlterTable
ALTER TABLE "FuelRecord" ADD COLUMN     "tenantId" UUID NOT NULL;

-- AlterTable
ALTER TABLE "MaintenanceRecord" ADD COLUMN     "tenantId" UUID NOT NULL;

-- AlterTable
ALTER TABLE "Trip" ADD COLUMN     "tenantId" UUID NOT NULL;

-- AddForeignKey
ALTER TABLE "Driver" ADD CONSTRAINT "Driver_tenantId_fkey" FOREIGN KEY ("tenantId") REFERENCES "Tenant"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Trip" ADD CONSTRAINT "Trip_tenantId_fkey" FOREIGN KEY ("tenantId") REFERENCES "Tenant"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "FuelRecord" ADD CONSTRAINT "FuelRecord_tenantId_fkey" FOREIGN KEY ("tenantId") REFERENCES "Tenant"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "MaintenanceRecord" ADD CONSTRAINT "MaintenanceRecord_tenantId_fkey" FOREIGN KEY ("tenantId") REFERENCES "Tenant"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Expense" ADD CONSTRAINT "Expense_tenantId_fkey" FOREIGN KEY ("tenantId") REFERENCES "Tenant"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
