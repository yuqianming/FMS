	function emptyFile(name,target){
		   var f=$("input[name='"+name+"']:file");
		   f.val("");
		   var cf=f.clone();
		   f.remove();
		   cf.appendTo("#"+target);
	}
	
	function fileIsEmptyOrNot(name)
	{
		var f=$("input[name='"+name+"']:file");
		if(f.val())
		{
			return true;
		}
		else
		{
			return false;
		}
	}