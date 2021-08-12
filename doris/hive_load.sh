#! /bin/bash

# $1: Doris 库, $2: Dorsi 表, $3: Hive: hdfs path
current_timeStamp=`date +%s`
#doris_table_name=$1
doris_db=$1
doris_tb=$2
# doris database and table
#doris_db_tb=(${doris_table_name//./ })
#doris_db=${doris_db_tb[0]}
#doris_tb=${doris_db_tb[1]}
hive_table_path=$3

# Doris label name
label_name="label_${doris_db}_${doris_tb}_${current_timeStamp}"

# Doris 环境配置
doris_server=10.0.15.133
doris_port=9030
doris_user=root
doris_password=root
doris_log_dir=$(cd "$(dirname "$0")";pwd)/doris-log
doris_log=${doris_log_dir}/doris-load-job_${doris_db}_${doris_tb}-$(date +"%Y-%m-%d-%H:%M:%S").log

function create_log_dir() {
    if [ ! -d ${doris_log_dir} ]; then
        mkdir -p ${doris_log_dir}
    fi
}
create_log_dir



# 参数校验, $1 参数格式: [库名.表名], eg [db.tb]
function check_param() {
    result=$(echo $1 | grep ".")
    if [[ "$result" == "" ]]; then
        echo "参数 $1 格式必须为: [database.table]'"
        exit 0
    fi
}
#check_param

``
# mysql cmd
mysql=`which mysql`

if [ -z ${mysql} ]; then
    echo "请使用命令: which mysql 确认当前环境的 MySql是否正常"
    exit 0
fi

mysql_cmd="${mysql} -h${doris_server} -P${doris_port} -u${doris_user} -p${doris_password}"
echo ${mysql_cmd}

# sql
sql_use_database="USE \`${doris_db}\`;"
sql_select_test="SELECT * FROM \`${doris_tb}\` LIMIT 10;"
sql_doris_hive_load="LOAD LABEL ${label_name} (
  DATA INFILE(\"${hive_table_path}\")
  INTO TABLE \`${doris_tb}\`
  FORMAT AS \"parquet\"
)
WITH BROKER broker_name
PROPERTIES(
  \"timeout\"=\"3600\",
  \"max_filter_ratio\"=\"0.1\"
);"
sql_hive_load_job_info="SHOW LOAD WHERE LABEL = '${label_name}' \G;"
sql_exit="\q;"

echo ${sql_hive_load_job_info}

# mysql 提交一个 hive load job
function submit_doris_load_job() {
  `${mysql_cmd} -e "
    ${sql_use_database}
    ${sql_doris_hive_load}
    ${sql_exit}"`
}
submit_doris_load_job

sleep 1

# 循环检查 job status, 一旦 job finished /cancelled 就停止调度任务
function check_doris_load_job_status() {
  `${mysql_cmd} -e "${sql_use_database} ${sql_hive_load_job_info} ${sql_exit}" > ${doris_log}`
  status=`sed -n "4p" ${doris_log}`
  while [[ $(echo $status | grep 'State') != "State: FINISHED" && $(echo $status | grep 'State') != "State: CANCELLED" ]]; do
    sleep 5
    `${mysql_cmd} -e "${sql_use_database} ${sql_hive_load_job_info} ${sql_exit}" > ${doris_log}`
    status=`sed -n "4p" ${doris_log}`
#    echo "Job status info: ${status}"
  done
}
check_doris_load_job_status
# rm -rf ${doris_log}
exit 0