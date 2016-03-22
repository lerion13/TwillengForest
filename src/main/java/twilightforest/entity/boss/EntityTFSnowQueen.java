package twilightforest.entity.boss;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.entity.IBreathAttacker;
import twilightforest.entity.ai.EntityAITFHoverBeam;
import twilightforest.entity.ai.EntityAITFHoverSummon;
import twilightforest.entity.ai.EntityAITFHoverThenDrop;
import twilightforest.entity.boss.EntityTFIceCrystal;
import twilightforest.entity.boss.EntityTFSnowQueenIceShield;
import twilightforest.item.TFItems;
import twilightforest.world.ChunkProviderTwilightForest;
import twilightforest.world.TFWorldChunkManager;
import twilightforest.world.WorldProviderTwilightForest;

public class EntityTFSnowQueen extends EntityMob implements IBossDisplayData, IEntityMultiPart, IBreathAttacker {

   private static final int MAX_SUMMONS = 6;
   private static final int BEAM_FLAG = 21;
   private static final int PHASE_FLAG = 22;
   private static final int MAX_DAMAGE_WHILE_BEAMING = 25;
   private static final float BREATH_DAMAGE = 4.0F;
   public Entity[] iceArray;
   private int summonsRemaining = 0;
   private int successfulDrops;
   private int maxDrops;
   private int damageWhileBeaming;


   public EntityTFSnowQueen(World par1World) {
      super(par1World);
      super.tasks.addTask(0, new EntityAISwimming(this));
      super.tasks.addTask(1, new EntityAITFHoverSummon(this, EntityPlayer.class, 1.0D));
      super.tasks.addTask(2, new EntityAITFHoverThenDrop(this, EntityPlayer.class, 80, 20));
      super.tasks.addTask(3, new EntityAITFHoverBeam(this, EntityPlayer.class, 80, 100));
      super.tasks.addTask(6, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
      super.tasks.addTask(7, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(8, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.setSize(0.7F, 2.2F);
      this.iceArray = new Entity[7];

      for(int i = 0; i < this.iceArray.length; ++i) {
         this.iceArray[i] = new EntityTFSnowQueenIceShield(this);
      }

      this.setCurrentPhase(EntityTFSnowQueen.Phase.SUMMON);
      super.isImmuneToFire = true;
      super.experienceValue = 317;
   }

   public boolean canBePushed() {
      return false;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(21, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(22, Byte.valueOf((byte)0));
   }

   protected boolean isAIEnabled() {
      return true;
   }

   protected String getLivingSound() {
      return "TwilightForest:mob.ice.noise";
   }

   protected String getHurtSound() {
      return "TwilightForest:mob.ice.hurt";
   }

   protected String getDeathSound() {
      return "TwilightForest:mob.ice.death";
   }

   protected Item getDropItem() {
      return Items.snowball;
   }

   protected void enchantEquipment() {
      super.enchantEquipment();
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      IEntityLivingData data = super.onSpawnWithEgg(par1EntityLivingData);
      return data;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();

      int look;
      float dist;
      float py;
      float px;
      for(look = 0; look < 3; ++look) {
         dist = (super.rand.nextFloat() - super.rand.nextFloat()) * 0.3F;
         py = this.getEyeHeight() + (super.rand.nextFloat() - super.rand.nextFloat()) * 0.5F;
         px = (super.rand.nextFloat() - super.rand.nextFloat()) * 0.3F;
         TwilightForestMod.proxy.spawnParticle(super.worldObj, "snowguardian", super.lastTickPosX + (double)dist, super.lastTickPosY + (double)py, super.lastTickPosZ + (double)px, 0.0D, 0.0D, 0.0D);
      }

      if(this.getCurrentPhase() == EntityTFSnowQueen.Phase.DROP) {
         for(look = 0; look < this.iceArray.length; ++look) {
            dist = (super.rand.nextFloat() - super.rand.nextFloat()) * 0.5F;
            py = (super.rand.nextFloat() - super.rand.nextFloat()) * 0.5F;
            px = (super.rand.nextFloat() - super.rand.nextFloat()) * 0.5F;
            TwilightForestMod.proxy.spawnParticle(super.worldObj, "snowwarning", this.iceArray[look].lastTickPosX + (double)dist, this.iceArray[look].lastTickPosY + (double)py, this.iceArray[look].lastTickPosZ + (double)px, 0.0D, 0.0D, 0.0D);
         }
      }

      if(this.isBreathing() && this.isEntityAlive()) {
         Vec3 var21 = this.getLookVec();
         double var22 = 0.5D;
         double var23 = super.posX + var21.xCoord * var22;
         double py1 = super.posY + 1.7000000476837158D + var21.yCoord * var22;
         double pz = super.posZ + var21.zCoord * var22;

         for(int i = 0; i < 10; ++i) {
            double dx = var21.xCoord;
            double dy = 0.0D;
            double dz = var21.zCoord;
            double spread = 2.0D + this.getRNG().nextDouble() * 2.5D;
            double velocity = 2.0D + this.getRNG().nextDouble() * 0.15D;
            dx += this.getRNG().nextGaussian() * 0.0075D * spread;
            dy += this.getRNG().nextGaussian() * 0.0075D * spread;
            dz += this.getRNG().nextGaussian() * 0.0075D * spread;
            dx *= velocity;
            dy *= velocity;
            dz *= velocity;
            TwilightForestMod.proxy.spawnParticle(super.worldObj, "icebeam", var23, py1, pz, dx, dy, dz);
         }
      }

   }

   public void onUpdate() {
      super.onUpdate();

      int k;
      for(k = 0; k < this.iceArray.length; ++k) {
         this.iceArray[k].onUpdate();
         if(k < this.iceArray.length - 1) {
            Vec3 d = this.getIceShieldPosition(k);
            this.iceArray[k].setPosition(d.xCoord, d.yCoord, d.zCoord);
            this.iceArray[k].rotationYaw = this.getIceShieldAngle(k);
         } else {
            this.iceArray[k].setPosition(super.posX, super.posY - 1.0D, super.posZ);
            this.iceArray[k].rotationYaw = this.getIceShieldAngle(k);
         }

         if(!super.worldObj.isRemote) {
            this.applyShieldCollisions(this.iceArray[k]);
         }
      }

      if(super.deathTime > 0) {
         for(k = 0; k < 5; ++k) {
            double var9 = super.rand.nextGaussian() * 0.02D;
            double d1 = super.rand.nextGaussian() * 0.02D;
            double d2 = super.rand.nextGaussian() * 0.02D;
            String explosionType = super.rand.nextBoolean()?"hugeexplosion":"explode";
            super.worldObj.spawnParticle(explosionType, super.posX + (double)(super.rand.nextFloat() * super.width * 2.0F) - (double)super.width, super.posY + (double)(super.rand.nextFloat() * super.height), super.posZ + (double)(super.rand.nextFloat() * super.width * 2.0F) - (double)super.width, var9, d1, d2);
         }
      }

   }

   protected void dropFewItems(boolean par1, int par2) {
      this.dropBow();
      int totalDrops = super.rand.nextInt(4 + par2) + 1;

      int i;
      for(i = 0; i < totalDrops; ++i) {
         this.dropItem(Item.getItemFromBlock(Blocks.packed_ice), 7);
      }

      totalDrops = super.rand.nextInt(5 + par2) + 5;

      for(i = 0; i < totalDrops; ++i) {
         this.dropItem(Items.snowball, 16);
      }

      this.entityDropItem(new ItemStack(TFItems.trophy, 1, 4), 0.0F);
   }

   private void dropBow() {
      int bowType = super.rand.nextInt(2);
      if(bowType == 0) {
         this.entityDropItem(new ItemStack(TFItems.tripleBow), 0.0F);
      } else {
         this.entityDropItem(new ItemStack(TFItems.seekerBow), 0.0F);
      }

   }

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      if(par1DamageSource.getSourceOfDamage() instanceof EntityPlayer) {
         ((EntityPlayer)par1DamageSource.getSourceOfDamage()).triggerAchievement(TFAchievementPage.twilightHunter);
         ((EntityPlayer)par1DamageSource.getSourceOfDamage()).triggerAchievement(TFAchievementPage.twilightProgressGlacier);
      }

      if(!super.worldObj.isRemote) {
         int dx = MathHelper.floor_double(super.posX);
         int dy = MathHelper.floor_double(super.posY);
         int dz = MathHelper.floor_double(super.posZ);
         if(super.worldObj.provider instanceof WorldProviderTwilightForest) {
            ChunkProviderTwilightForest chunkProvider = ((WorldProviderTwilightForest)super.worldObj.provider).getChunkProvider();
            TFFeature nearbyFeature = ((TFWorldChunkManager)super.worldObj.provider.worldChunkMgr).getFeatureAt(dx, dz, super.worldObj);
            if(nearbyFeature == TFFeature.lichTower) {
               chunkProvider.setStructureConquered(dx, dy, dz, true);
            }
         }
      }

   }

   private void applyShieldCollisions(Entity collider) {
      List list = super.worldObj.getEntitiesWithinAABBExcludingEntity(collider, collider.boundingBox.expand(-0.20000000298023224D, -0.20000000298023224D, -0.20000000298023224D));
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         Entity collided = (Entity)var3.next();
         if(collided.canBePushed()) {
            this.applyShieldCollision(collider, collided);
         }
      }

   }

   protected void applyShieldCollision(Entity collider, Entity collided) {
      if(collided != this) {
         collided.applyEntityCollision(collider);
         if(collided instanceof EntityLivingBase) {
            boolean attackSuccess = super.attackEntityAsMob(collided);
            if(attackSuccess) {
               collided.motionY += 0.4000000059604645D;
               this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
            }
         }
      }

   }

   protected void updateAITasks() {
      super.updateAITasks();
      if(this.getCurrentPhase() == EntityTFSnowQueen.Phase.SUMMON && this.getSummonsRemaining() == 0 && this.countMyMinions() <= 0) {
         this.setCurrentPhase(EntityTFSnowQueen.Phase.DROP);
      }

      if(this.getCurrentPhase() == EntityTFSnowQueen.Phase.DROP && this.successfulDrops >= this.maxDrops) {
         this.setCurrentPhase(EntityTFSnowQueen.Phase.BEAM);
      }

      if(this.getCurrentPhase() == EntityTFSnowQueen.Phase.BEAM && this.damageWhileBeaming >= 25) {
         this.setCurrentPhase(EntityTFSnowQueen.Phase.SUMMON);
      }

   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float damage) {
      boolean result = super.attackEntityFrom(par1DamageSource, damage);
      if(result && this.getCurrentPhase() == EntityTFSnowQueen.Phase.BEAM) {
         this.damageWhileBeaming = (int)((float)this.damageWhileBeaming + damage);
      }

      return result;
   }

   private Vec3 getIceShieldPosition(int i) {
      return this.getIceShieldPosition(this.getIceShieldAngle(i), 1.0F);
   }

   private float getIceShieldAngle(int i) {
      return 60.0F * (float)i + (float)super.ticksExisted * 5.0F;
   }

   public Vec3 getIceShieldPosition(float angle, float distance) {
      double var1 = Math.cos((double)angle * 3.141592653589793D / 180.0D) * (double)distance;
      double var3 = Math.sin((double)angle * 3.141592653589793D / 180.0D) * (double)distance;
      return Vec3.createVectorHelper(super.posX + var1, super.posY + this.getShieldYOffset(), super.posZ + var3);
   }

   public double getShieldYOffset() {
      return 0.10000000149011612D;
   }

   protected void fall(float par1) {}

   public World func_82194_d() {
      return super.worldObj;
   }

   public boolean attackEntityFromPart(EntityDragonPart entitydragonpart, DamageSource damagesource, float i) {
      return false;
   }

   public Entity[] getParts() {
      return this.iceArray;
   }

   public boolean destroyBlocksInAABB(AxisAlignedBB par1AxisAlignedBB) {
      int minX = MathHelper.floor_double(par1AxisAlignedBB.minX);
      int minY = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int minZ = MathHelper.floor_double(par1AxisAlignedBB.minZ);
      int maxX = MathHelper.floor_double(par1AxisAlignedBB.maxX);
      int maxY = MathHelper.floor_double(par1AxisAlignedBB.maxY);
      int maxZ = MathHelper.floor_double(par1AxisAlignedBB.maxZ);
      boolean wasBlocked = false;

      for(int dx = minX; dx <= maxX; ++dx) {
         for(int dy = minY; dy <= maxY; ++dy) {
            for(int dz = minZ; dz <= maxZ; ++dz) {
               Block block = super.worldObj.getBlock(dx, dy, dz);
               if(block != Blocks.air) {
                  int currentMeta = super.worldObj.getBlockMetadata(dx, dy, dz);
                  if(block != Blocks.ice && block != Blocks.packed_ice) {
                     wasBlocked = true;
                  } else {
                     //super.worldObj.setBlock(dx, dy, dz, Blocks.air, 0, 2); //lerion fix
                     super.worldObj.playAuxSFX(2001, dx, dy, dz, Block.getIdFromBlock(block) + (currentMeta << 12));
                  }
               }
            }
         }
      }

      return wasBlocked;
   }

   public boolean isBreathing() {
      return this.getDataWatcher().getWatchableObjectByte(21) == 1;
   }

   public void setBreathing(boolean flag) {
      this.getDataWatcher().updateObject(21, Byte.valueOf((byte)(flag?1:0)));
   }

   public EntityTFSnowQueen.Phase getCurrentPhase() {
      return EntityTFSnowQueen.Phase.values()[this.getDataWatcher().getWatchableObjectByte(22)];
   }

   public void setCurrentPhase(EntityTFSnowQueen.Phase currentPhase) {
      this.getDataWatcher().updateObject(22, Byte.valueOf((byte)currentPhase.ordinal()));
      if(currentPhase == EntityTFSnowQueen.Phase.SUMMON) {
         this.setSummonsRemaining(6);
      }

      if(currentPhase == EntityTFSnowQueen.Phase.DROP) {
         this.successfulDrops = 0;
         this.maxDrops = 2 + super.rand.nextInt(3);
      }

      if(currentPhase == EntityTFSnowQueen.Phase.BEAM) {
         this.damageWhileBeaming = 0;
      }

   }

   public int getSummonsRemaining() {
      return this.summonsRemaining;
   }

   public void setSummonsRemaining(int summonsRemaining) {
      this.summonsRemaining = summonsRemaining;
   }

   public void summonMinionAt(EntityLivingBase targetedEntity) {
      Vec3 minionSpot = this.findVecInLOSOf(targetedEntity);
      EntityTFIceCrystal minion = new EntityTFIceCrystal(super.worldObj);
      minion.setPosition(minionSpot.xCoord, minionSpot.yCoord, minionSpot.zCoord);
      super.worldObj.spawnEntityInWorld(minion);
      minion.setAttackTarget(targetedEntity);
      --this.summonsRemaining;
   }

   protected Vec3 findVecInLOSOf(Entity targetEntity) {
      if(targetEntity == null) {
         return null;
      } else {
         double tx = 0.0D;
         double ty = 0.0D;
         double tz = 0.0D;
         byte tries = 100;

         for(int i = 0; i < tries; ++i) {
            tx = targetEntity.posX + super.rand.nextGaussian() * 16.0D;
            ty = targetEntity.posY + super.rand.nextGaussian() * 8.0D;
            tz = targetEntity.posZ + super.rand.nextGaussian() * 16.0D;
            boolean groundFlag = false;
            int bx = MathHelper.floor_double(tx);
            int by = MathHelper.floor_double(ty);
            int bz = MathHelper.floor_double(tz);

            while(!groundFlag && ty > 0.0D) {
               Block halfWidth = super.worldObj.getBlock(bx, by - 1, bz);
               if(halfWidth != Blocks.air && halfWidth.getMaterial().isSolid()) {
                  groundFlag = true;
               } else {
                  --ty;
                  --by;
               }
            }

            if(by != 0 && this.canEntitySee(targetEntity, tx, ty, tz)) {
               float var16 = super.width / 2.0F;
               AxisAlignedBB destBox = AxisAlignedBB.getBoundingBox(tx - (double)var16, ty - (double)super.yOffset + (double)super.ySize, tz - (double)var16, tx + (double)var16, ty - (double)super.yOffset + (double)super.ySize + (double)super.height, tz + (double)var16);
               if(super.worldObj.getCollidingBoundingBoxes(this, destBox).size() <= 0 && !super.worldObj.isAnyLiquid(destBox)) {
                  break;
               }
            }
         }

         return tries == 99?null:Vec3.createVectorHelper(tx, ty, tz);
      }
   }

   protected boolean canEntitySee(Entity entity, double dx, double dy, double dz) {
      return super.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), Vec3.createVectorHelper(dx, dy, dz)) == null;
   }

   public int countMyMinions() {
      List nearbyMinons = super.worldObj.getEntitiesWithinAABB(EntityTFIceCrystal.class, AxisAlignedBB.getBoundingBox(super.posX, super.posY, super.posZ, super.posX + 1.0D, super.posY + 1.0D, super.posZ + 1.0D).expand(32.0D, 16.0D, 32.0D));
      return nearbyMinons.size();
   }

   public void incrementSuccessfulDrops() {
      ++this.successfulDrops;
   }

   public void doBreathAttack(Entity target) {
      if(target.attackEntityFrom(DamageSource.causeMobDamage(this), 4.0F)) {
         ;
      }

   }

   public static enum Phase {

      SUMMON("SUMMON", 0),
      DROP("DROP", 1),
      BEAM("BEAM", 2);
      // $FF: synthetic field
      private static final EntityTFSnowQueen.Phase[] $VALUES = new EntityTFSnowQueen.Phase[]{SUMMON, DROP, BEAM};


      private Phase(String var1, int var2) {}

   }
}
