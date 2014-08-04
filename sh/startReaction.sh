PNAME=virotest
ENV=prod

echo "starting ${PNAME}"

pid=`ps -ef | grep java | grep "${PNAME}" | awk '{print $2}'`

if [ "a$pid" = "a" ]; then 
	nohup java -Denv=${ENV} -Dn=${PNAME} -Xmx4000m  -cp ./:./conf/:./lib/* cn.cnic.virostudio.StartReaction >stdout 2>stderr &
	echo "started ${PNAME} Entire."
	exit 0
fi

echo "ERROR: has been started:$pid"
exit 1

