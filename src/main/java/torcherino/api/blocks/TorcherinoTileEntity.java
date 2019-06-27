package torcherino.api.blocks;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import torcherino.Utilities;
import torcherino.api.Tier;
import torcherino.blocks.Blocks;
import javax.annotation.Nullable;

public class TorcherinoTileEntity extends TileEntity implements INameable, ITickable
{
	private ITextComponent customName;
	private int xRange, yRange, zRange, speed, redstoneMode;
	private boolean active;
	private Tier tier;

	public TorcherinoTileEntity()
	{
		super(Blocks.INSTANCE.TORCHERINO_TILE_ENTITY_TYPE);
	}

	@Override public ITextComponent getName(){ return hasCustomName() ? customName : new TextComponentTranslation(world.getBlockState(pos).getBlock().getTranslationKey()); }

	@Override public boolean hasCustomName()
	{
		return customName != null;
	}

	@Nullable @Override public ITextComponent getCustomName()
	{
		return customName;
	}

	public void setCustomName(@Nullable ITextComponent customName)
	{
		this.customName = customName;
	}

	public Tier getTier()
	{
		if (tier == null)
		{
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof LanterinoBlock) tier = ((LanterinoBlock) block).getTier();
			else if (block instanceof TorcherinoBlock) tier = ((TorcherinoBlock) block).getTier();
			else if (block instanceof TorcherinoWallBlock) tier = ((TorcherinoWallBlock) block).getTier();
		}
		return tier;
	}

	public int getxRange(){ return xRange; }

	public int getzRange(){ return zRange; }

	public int getyRange(){ return yRange; }

	public int getSpeed(){ return speed; }

	public int getRedstoneMode(){ return redstoneMode; }

	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		if (compound.contains("CustomName", 8))
		{
			setCustomName(ITextComponent.Serializer.fromJson(compound.getString("CustomName")));
		}
		this.xRange = compound.getInt("XRange");
		this.zRange = compound.getInt("ZRange");
		this.yRange = compound.getInt("YRange");
		this.speed = compound.getInt("Speed");
		this.redstoneMode = compound.getInt("RedstoneMode");
	}

	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		if (hasCustomName())
		{
			compound.setString("CustomName", ITextComponent.Serializer.toJson(getCustomName()));
		}
		compound.setInt("XRange", this.xRange);
		compound.setInt("ZRange", this.zRange);
		compound.setInt("YRange", this.yRange);
		compound.setInt("Speed", this.speed);
		compound.setInt("RedstoneMode", this.redstoneMode);
		return compound;
	}

	public void readClientData(int xRange, int zRange, int yRange, int speed, int redstoneMode)
	{
		this.xRange = xRange;
		this.zRange = zRange;
		this.yRange = yRange;
		this.speed = speed;
		this.redstoneMode = redstoneMode;
		this.markDirty();
	}

	@Override public void tick()
	{

	}

	public void setPoweredByRedstone(boolean powered)
	{
		switch(redstoneMode)
		{

			case 0:
				this.active = powered;
				break;
			case 1:
				this.active = !powered;
				break;
			case 2:
				this.active = true;
				break;
			case 3:
				this.active = false;
				break;
		}
		Utilities.LOGGER.info("New State: {}", active);
	}
}
