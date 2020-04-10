import numpy

def rand(np, up):
    np = np * 843314861 + 453816693
    if np < 0:
        np = np +2147483647
        np = np + 1
    up = (float(np*0.4656612e-9))
    return np, up


if __name__ == "__main__":
    print("난수 발생기 테스트")
    num = 0
    up = 0
    num, up = rand(num, up)
    print(num)
    print(up)