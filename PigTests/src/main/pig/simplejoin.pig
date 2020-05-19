userdetails = load '../../input/UserDetails.txt' as (mobile, username);

deliverydetails = load '../../input/DeliveryDetails.txt' as (mobile , deliverycode);

userdeliverydetails = join userdetails BY mobile full, deliverydetails BY mobile;

describe userdeliverydetails;
--userdeliverydetails: {userdetails::mobile: bytearray, userdetails::username: bytearray,
--                      deliverydetails::mobile: bytearray,deliverydetails::deliverycode: bytearray}

deliverystatuscodes = load '../../input/DeliveryStatusCodes.txt' as (deliverycode, message);

thingy = join userdeliverydetails by $3 full, deliverystatuscodes by $0;

describe thingy;
